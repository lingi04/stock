package com.jh.stock.batch.service;

import com.jh.stock.domain.*;
import com.jh.stock.domain.param.IndicatorsParam;
import com.jh.stock.domain.param.SRimCreateParam;
import com.jh.stock.domain.service.IndicatorsService;
import com.slack.api.Slack;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.HeaderBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import com.slack.api.model.block.composition.PlainTextObject;
import com.slack.api.model.block.composition.TextObject;
import com.sun.istack.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {
    @Value("${slack.token}")
    private String token;

    private final IndicatorsService indicatorsService;

    public void sendReport(Favorites favorites, List<MyStock> myFavoriteStockList, Map<String, List<Indicators>> indicatorsMap, Map<String, MyQuote> quoteMap) {
        myFavoriteStockList.forEach(myStock -> {
            reportUsingSlack(
                myStock.getName(),
                myStock.getTicker(),
                favorites.getIndicatorReportList(),
                indicatorsMap.getOrDefault(myStock.getTicker(), Collections.emptyList()),
                favorites.getSlackChannel(),
                quoteMap.getOrDefault(myStock.getTicker(), MyQuote.emptyQuote())
            );
        });
    }

    private void reportUsingSlack(String name, String ticker, List<IndicatorReportType> indicatorReportList, List<Indicators> indicatorList, String channel, @NotNull MyQuote myQuote) {
        SimpleDateFormat s = new SimpleDateFormat("yy.MM.dd");
        String today = s.format(Timestamp.valueOf(LocalDateTime.now()));
        String title = name + "[" + ticker + "] - " + today;
        LayoutBlock headerBlock = HeaderBlock.builder().text(PlainTextObject.builder().text(title).build()).build();

        TextObject quoteData = MarkdownTextObject.builder().text("*주가*\n" + addArrowEmoji(myQuote.getFluctuation()) + myQuote.toStringMarketPriceAndFluctuation()).build();
        TextObject properPriceData = MarkdownTextObject.builder().text(getProperPrice(indicatorsService.filterAndSortIndicatorList(indicatorList, IndicatorInterval.YEAR))).build();
        LayoutBlock commonBLock = SectionBlock.builder().fields(List.of(quoteData, properPriceData)).build();

        Slack slack = Slack.getInstance();
        try {
            ChatPostMessageResponse response = slack.methods(token)
                .chatPostMessage(req -> req.channel(channel).blocks(List.of(
                    headerBlock,
                    commonBLock,
                    getIndicatorsBlock(IndicatorInterval.YEAR, indicatorList, indicatorReportList),
                    getIndicatorsBlock(IndicatorInterval.QUARTER, indicatorList, indicatorReportList),
                    DividerBlock.builder().build()))
                );
        } catch (Exception e) {
            log.error("[ReportService][reportUsingSlack] {}", e.getMessage());
        }
    }

    private String getProperPrice(List<Indicators> indicatorsList) {
        List<BigDecimal> persistenceFactorList = List.of(BigDecimal.ONE, new BigDecimal("0.9"), new BigDecimal("0.8"));

        String text = "*SRIM*\n";

        if (Optional.ofNullable(indicatorsList).isPresent()) {
            text += persistenceFactorList.stream()
                .map(factor -> SRim.from(SRimCreateParam.builder()
                    .persistenceFactor(factor)
                    .requiredYield(new BigDecimal("8.06"))
                    .expectedRoe(indicatorsService.calculateExpectedRoe(indicatorsList.stream()
                        .map(Indicators::getRoe)
                        .collect(Collectors.toList()), indicatorsService.getRoeTrend(indicatorsList))
                    )
                    .latestShareOfControllingShareholder(indicatorsService.getLatestShareOfControllingShareholder(indicatorsList))
                    .numberOfIssuedShares(indicatorsService.getLatestNumberOfIssuedShares(indicatorsList))
                    .latestYear(indicatorsService.getLatestYear(indicatorsList))
                    .build()))
                .map(sRim -> toReadableNumber(sRim.getProperPrice()) + "(지속계수 : " + sRim.getPersistenceFactor().toString() + ")")
                .collect(Collectors.joining("\n"));
        }

        return text;
    }

    private String toReadableNumber(@NonNull final BigDecimal n) {
        String[] arr = n.toString().split("[.]");

        char[] cArr = arr[0].toCharArray();
        int reverseIndex = 0;
        StringBuffer sb = new StringBuffer();
        for (int i = cArr.length - 1; i >= 0; i--) {
            if (reverseIndex != 0 && reverseIndex % 3 == 0 && reverseIndex != cArr.length - 1) {
                sb.append(",");
            }
            sb.append(cArr[i]);
            reverseIndex++;
        }

        return sb.reverse().toString();
    }

    private LayoutBlock getIndicatorsBlock(IndicatorInterval indicatorInterval, List<Indicators> indicatorsList, List<IndicatorReportType> indicatorReportList) {

        Map<IndicatorType, List<Indicators>> indicatorsListMapByIndicatorType = indicatorsList.stream()
            .filter(indicators -> indicators.getIssuanceCycle() == indicatorInterval)
            .collect(groupingBy(Indicators::getIndicatorType));

        Optional<Indicators> expectedLatest = indicatorsService.getExpectedLatestIndicators(indicatorsListMapByIndicatorType.getOrDefault(IndicatorType.EXPECTED, Collections.emptyList()));

        TextObject indicatorsYearlyHeader = MarkdownTextObject.builder().text(
            "*재무제표 - " + indicatorInterval + "*\n"
        ).build();
        TextObject dateContext = MarkdownTextObject.builder().text(
            indicatorsDateAsStr(indicatorsListMapByIndicatorType.getOrDefault(IndicatorType.CONFIRMED, Collections.emptyList()), expectedLatest) + "\n"
        ).build();
        TextObject content = MarkdownTextObject.builder().text(
            indicatorsReportAsStr(indicatorsListMapByIndicatorType.getOrDefault(IndicatorType.CONFIRMED, Collections.emptyList()), expectedLatest, indicatorReportList)
        ).build();
        return SectionBlock.builder().fields(
            List.of(
                indicatorsYearlyHeader,
                dateContext,
                content
            )
        ).build();
    }

    private String addArrowEmoji(BigDecimal fluctuation) {
        String direction = "";
        if (fluctuation.compareTo(BigDecimal.ZERO) > 0) {
            direction = ":arrow_up:";
        } else if (fluctuation.compareTo(BigDecimal.ZERO) < 0) {
            direction = ":arrow_down:";
        }

        return direction;
    }

    private String indicatorsReportAsStr(@NonNull List<Indicators> indicatorsList, Optional<Indicators> expectedIndicators, @NonNull List<IndicatorReportType> indicatorReportList) {
        if (indicatorsList.size() == 0) {
            return "-";
        }

        List<Indicators> dateDescSortedList = indicatorsService.dateDesc(indicatorsList);

        Indicators i1 = dateDescSortedList.get(0);
        Indicators i2;
        if (dateDescSortedList.size() > 1) {
            i2 = dateDescSortedList.get(1);
        } else {
            i2 = ExpectedIndicators.from(IndicatorsParam.builder().build());
        }

        return indicatorReportList.stream().map(report -> report.getDescribe() + ": " + report.getContent(i1, i2, expectedIndicators)).collect(Collectors.joining("\n"));
    }

    private String indicatorsDateAsStr(@NonNull List<Indicators> indicatorsList, Optional<Indicators> expectedIndicators) {
        String rtnStr = "";

        if (indicatorsList.size() == 0) {
            rtnStr += "YY.mm-YY.mm";
        }

        List<Indicators> dateDescSortedList = indicatorsService.dateDesc(indicatorsList);

        Indicators i1 = dateDescSortedList.get(0);
        String afterDate = i1.indicatorsDate();
        String beforeDate = null;
        if (dateDescSortedList.size() > 1) {
            beforeDate = dateDescSortedList.get(1).indicatorsDate();
        }
        if (beforeDate == null) {
            rtnStr += afterDate;
        } else {
            rtnStr += beforeDate + "-" + afterDate;
        }

        if (expectedIndicators.isPresent()) {
            rtnStr += "-" + expectedIndicators.get().indicatorsDate();
        }

        return rtnStr;
    }
}
