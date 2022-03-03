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
                indicatorsMap.getOrDefault(myStock.getTicker(), Collections.emptyList()).stream()
                    .filter(Indicators::isConfirmed)
                    .collect(groupingBy(Indicators::getIssuanceCycle)),
                favorites.getSlackChannel(),
                quoteMap.getOrDefault(myStock.getTicker(), MyQuote.emptyQuote())
            );
        });
    }

    private void reportUsingSlack(String name, String ticker, List<IndicatorReportType> indicatorReportList, Map<IndicatorInterval, List<Indicators>> indicatorListMap, String channel, @NotNull MyQuote myQuote) {
        SimpleDateFormat s = new SimpleDateFormat("yy.MM.dd");
        String today = s.format(Timestamp.valueOf(LocalDateTime.now()));
        String title = name + "[" + ticker + "] - " + today;
        LayoutBlock headerBlock = HeaderBlock.builder().text(PlainTextObject.builder().text(title).build()).build();


        TextObject quoteData = MarkdownTextObject.builder().text("*주가*\n" + addArrowEmoji(myQuote.getFluctuation()) + myQuote.toStringMarketPriceAndFluctuation()).build();
        TextObject properPriceData = MarkdownTextObject.builder().text(getProperPrice(indicatorListMap.get(IndicatorInterval.YEAR))).build();
        LayoutBlock commonBLock = SectionBlock.builder().fields(List.of(quoteData, properPriceData)).build();

        Slack slack = Slack.getInstance();
        try {
            ChatPostMessageResponse response = slack.methods(token)
                .chatPostMessage(req -> req.channel(channel).blocks(List.of(
                    headerBlock,
                    commonBLock,
                    getIndicatorsBlock(IndicatorInterval.YEAR, indicatorListMap, indicatorReportList),
                    getIndicatorsBlock(IndicatorInterval.QUARTER, indicatorListMap, indicatorReportList),
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

    private LayoutBlock getIndicatorsBlock(IndicatorInterval indicatorInterval, Map<IndicatorInterval, List<Indicators>> indicatorListMap, List<IndicatorReportType> indicatorReportList) {
        TextObject indicatorsYearlyHeader = MarkdownTextObject.builder().text(
            "*재무제표 - YEAR*\n"
        ).build();
        TextObject dateContext = MarkdownTextObject.builder().text(
            indicatorsDateAsStr(indicatorListMap.getOrDefault(indicatorInterval, Collections.emptyList())) + "\n"
        ).build();
        TextObject content = MarkdownTextObject.builder().text(
            indicatorsReportAsStr(indicatorListMap.getOrDefault(indicatorInterval, Collections.emptyList()), indicatorReportList)
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

    private String indicatorsReportAsStr(@NonNull List<Indicators> indicatorsList, @NonNull List<IndicatorReportType> indicatorReportList) {
        if (indicatorsList.size() == 0) {
            return "-";
        }
        Indicators i1 = indicatorsList.get(0);
        Indicators i2;
        if (indicatorsList.size() > 1) {
            i2 = indicatorsList.get(1);
        } else {
            i2 = ExpectedIndicators.from(IndicatorsParam.builder().build());
        }

        return indicatorReportList.stream().map(report -> report.getDescribe() + ": " + report.getContent(i1, i2)).collect(Collectors.joining("\n"));
    }

    private String indicatorsDateAsStr(@NonNull List<Indicators> indicatorsList) {
        if (indicatorsList.size() == 0) {
            return "-";
        }

        SimpleDateFormat s = new SimpleDateFormat("yy.MM.dd");

        Indicators i1 = indicatorsList.get(0);
        String afterDate = s.format(i1.getCreateTime());
        String startDate = null;
        if (indicatorsList.size() > 1) {
            startDate = s.format(indicatorsList.get(1).getCreateTime());
        }
        if (startDate == null) {
            return afterDate;
        } else {
            return startDate + "-" + afterDate;
        }
    }
}
