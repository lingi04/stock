package com.jh.stock.batch.job.indicators.processor;

import com.jh.stock.batch.dto.IndicatorsDto;
import com.jh.stock.batch.service.CrawlingService;
import com.jh.stock.batch.service.ParsingService;
import com.jh.stock.domain.IndicatorType;
import com.jh.stock.domain.Indicators;
import com.jh.stock.domain.MyStock;
import com.jh.stock.domain.service.IndicatorsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class IndicatorsProcessor implements ItemProcessor<MyStock, List<Indicators>> {
    private final CrawlingService crawlingService;
    private final IndicatorsService indicatorsService;
    private final ParsingService parsingService;

    private String NAVER_FINANCIAL_URL = "https://finance.naver.com/item/main.nhn?code=%s";
    private String FN_GUIDE_URL = "http://comp.fnguide.com/SVO2/ASP/SVD_Main.asp?gicode=A%s";

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
        indicatorsDtoMap.getOrDefault(IndicatorType.CONFIRMED, Collections.emptyList()).forEach(dto -> {
            Indicators indicators = dto.toConfirmedIndicators();
            log.debug("hashcode : {}", indicators.hashCode());
            if (!confirmedIndicators.contains(indicators)) {
                indicators.initializeStocks(myStock);
                indicatorsList.add(indicators);
            }
        });

        // process expected indicators
        List<Indicators> expectedIndicators = indicatorsService.findAllByTickerAndBusinessYearInAndType(
            myStock.getTicker(),
            indicatorsDtoMap.getOrDefault(IndicatorType.EXPECTED, Collections.emptyList()).stream().map(IndicatorsDto::getBusinessYear).collect(Collectors.toList()),
            IndicatorType.EXPECTED
        );
        indicatorsDtoMap.getOrDefault(IndicatorType.EXPECTED, Collections.emptyList()).forEach(dto -> {
            Indicators indicators = dto.toExpectedIndicators();
            if (!expectedIndicators.contains(indicators)) {
                indicators.initializeStocks(myStock);
                indicatorsList.add(indicators);
            }
        });

        Thread.sleep(50);

        return indicatorsList;
    }
}
