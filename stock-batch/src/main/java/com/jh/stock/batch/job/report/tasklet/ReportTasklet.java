package com.jh.stock.batch.job.report.tasklet;

import com.jh.stock.batch.service.ReportService;
import com.jh.stock.domain.Favorites;
import com.jh.stock.domain.Indicators;
import com.jh.stock.domain.MyQuote;
import com.jh.stock.domain.MyStock;
import com.jh.stock.domain.repository.FavoriteRepository;
import com.jh.stock.domain.repository.IndicatorsRepository;
import com.jh.stock.domain.repository.MyStockRepository;
import com.jh.stock.domain.service.MyQuoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReportTasklet implements Tasklet, StepExecutionListener {
    private final MyStockRepository stockRepository;
    private final FavoriteRepository favoriteRepository;
    private final ReportService reportService;
    private final IndicatorsRepository indicatorsRepository;
    private final MyQuoteService myQuoteService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Iterable<Favorites> favoritesList = favoriteRepository.findAll();
        Set<String> tickerSet = StreamSupport.stream(favoritesList.spliterator(), false).flatMap(favorites -> favorites.getTickerList().stream()).collect(Collectors.toSet());
        List<MyStock> myStockList = stockRepository.findMyStockByTickerIn(tickerSet);
        Map<String, List<Indicators>> indicatorsMap = indicatorsRepository.findAllByTickerIn(tickerSet).stream().collect(groupingBy(Indicators::getTicker));
        Map<String, MyQuote> quoteMap = myQuoteService.getLatestQuoteByTicker(tickerSet);

        favoritesList.forEach(favorites -> {
            List<MyStock> myFavoriteStockList = myStockList.stream().filter(myStock -> favorites.getTickerList().contains(myStock.getTicker())).collect(Collectors.toList());
            reportService.sendReport(favorites, myFavoriteStockList, indicatorsMap, quoteMap);
        });

        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
