package com.jh.stock.batch.job.indicators.processor;

import com.jh.stock.batch.TriFunction;
import com.jh.stock.batch.dto.IndicatorsDto;
import com.jh.stock.batch.service.CrawlingService;
import com.jh.stock.batch.service.ParsingService;
import com.jh.stock.domain.IndicatorType;
import com.jh.stock.domain.Indicators;
import com.jh.stock.domain.MyStock;
import com.jh.stock.domain.repository.MyStockRepository;
import com.jh.stock.domain.service.IndicatorsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class IndicatorsProcessor implements ItemProcessor<MyStock, List<Indicators>> {
    private final CrawlingService crawlingService;
    private final IndicatorsService indicatorsService;
    private final ParsingService parsingService;
    private final MyStockRepository myStockRepository;

    private String NAVER_FINANCIAL_URL = "https://finance.naver.com/item/main.nhn?code=%s";
    private String FN_GUIDE_URL = "http://comp.fnguide.com/SVO2/ASP/SVD_Main.asp?gicode=A%s";

    public final TriFunction<MyStock, List<IndicatorsDto>, Set<Indicators>, List<Indicators>> getNewConfirmedIndicatorList =
        ((myStock, indicatorsDtoList, confirmedIndicators) -> indicatorsDtoList.stream()
            .map(indicatorsDto -> {
                Indicators indicators = indicatorsDto.toConfirmedIndicators();
                indicators.initializeStocks(myStock);
                return indicators;
            })
            .filter(indicators -> !confirmedIndicators.contains(indicators))
            .collect(Collectors.toList())
        );
    public final TriFunction<MyStock, List<IndicatorsDto>, Set<Indicators>, List<Indicators>> getNewExpectedIndicatorList =
        ((myStock, indicatorsDtoList, expectedIndicators) -> indicatorsDtoList.stream()
            .map(indicatorsDto -> {
                Indicators indicators = indicatorsDto.toExpectedIndicators();
                indicators.initializeStocks(myStock);
                return indicators;
            })
            .filter(indicators -> !expectedIndicators.contains(indicators))
            .collect(Collectors.toList())
        );


    @Override
    public List<Indicators> process(MyStock myStock) throws Exception {
        Document document = crawlingService.crawlingData(FN_GUIDE_URL, myStock.getTicker());
        String[] a = document.select("#div1 #svdMainGrid1 tbody tr").eq(6).select("td.r").eq(0).text().split("/");
        Long count1 = 0l;
        Long count2 = 0l;

        try {
            count1 = Long.parseLong(a[0].trim().replaceAll(",", ""));
        } catch (Exception e) {

        }

        try {
            count2 = Long.parseLong(a[1].trim().replaceAll(",", ""));
        } catch (Exception e) {

        }

        Map<IndicatorType, List<IndicatorsDto>> indicatorsDtoMap = parsingService.fnGuideDocParsing(
            document,
            myStock.getTicker(),
            count1 + count2
        ).stream().collect(Collectors.groupingBy(IndicatorsDto::getIndicatorType));
        List<Indicators> indicatorsList = new ArrayList<>();


        // process confirmed indicators
        List<Integer> list = indicatorsDtoMap.getOrDefault(IndicatorType.CONFIRMED, Collections.emptyList())
            .stream().map(IndicatorsDto::getBusinessYear)
            .collect(Collectors.toList());
        Set<Indicators> confirmedIndicators = new HashSet<>(indicatorsService.findAllByTickerAndBusinessYearInAndType(
            myStock.getTicker(), list, IndicatorType.CONFIRMED
        ));
        // process expected indicators
        Set<Indicators> expectedIndicators = new HashSet(indicatorsService.findAllByTickerAndBusinessYearInAndType(
            myStock.getTicker(),
            indicatorsDtoMap.getOrDefault(IndicatorType.EXPECTED, Collections.emptyList()).stream().map(IndicatorsDto::getBusinessYear).collect(Collectors.toList()),
            IndicatorType.EXPECTED
        ));

        List<Indicators> newConfirmedIndicators = getNewConfirmedIndicatorList.apply(myStock, indicatorsDtoMap.getOrDefault(IndicatorType.CONFIRMED, Collections.emptyList()), confirmedIndicators);
        List<Indicators> newExpectedIndicators = getNewExpectedIndicatorList.apply(myStock, indicatorsDtoMap.getOrDefault(IndicatorType.EXPECTED, Collections.emptyList()), expectedIndicators);

        indicatorsDtoMap.values().stream()
            .flatMap(l -> l.stream())
            .findAny()
            .ifPresent(iDto -> {
                myStock.updateNumberOfIssuedShares(iDto.getNumberOfIssuedShares());
            });

        myStockRepository.save(myStock);

        Thread.sleep(50);

        return Stream.concat(newConfirmedIndicators.stream(), newExpectedIndicators.stream()).collect(Collectors.toList());
    }
}
