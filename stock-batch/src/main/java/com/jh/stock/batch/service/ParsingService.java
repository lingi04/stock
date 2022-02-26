package com.jh.stock.batch.service;

import com.jh.stock.batch.dto.IndicatorsDto;
import com.jh.stock.domain.IndicatorInterval;
import com.jh.stock.domain.IndicatorType;
import com.jh.stock.domain.ReportType;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ParsingService {
    public List<IndicatorsDto> fnGuideDocParsing(Document document, String ticker, Long numberOfIssuedShares) {
        Elements tableElements = document.select(".section.ul_de #div15 .um_table .us_table_ty1.h_fix.zigbg_no").eq(0);

        Elements theadElements = getFnGuideThead(tableElements);
        if (theadElements.size() == 0) {
            return Collections.emptyList();
        }
        Elements tbodyElements = getTbody(tableElements);
        if (tbodyElements.size() == 0) {
            return Collections.emptyList();
        }

        Elements firstTheadElements = theadElements.select(".th2row_f").eq(0);
        Elements issuedYearDataElements = theadElements.select(".td_gapcolor2").eq(0).select("div");

        ReportType reportType = ReportType.resolve(firstTheadElements.select("th").eq(0).text());
        int annualReportCount = Integer.parseInt(firstTheadElements.select("th").eq(1).attr("colspan"));
        int quarterlyReportCount = Integer.parseInt(firstTheadElements.select("th").eq(2).attr("colspan"));

        List<IndicatorsDto> indicatorsDtoList = new ArrayList<>();
        setReportType(indicatorsDtoList, reportType, annualReportCount, quarterlyReportCount);
        setTicker(indicatorsDtoList, ticker);
        setFnGuideIssuedYeartDate(indicatorsDtoList, issuedYearDataElements);
        setIndicators(indicatorsDtoList, tbodyElements);
        indicatorsDtoList.forEach(dto -> dto.setNumberOfIssuedShares(numberOfIssuedShares));

        return indicatorsDtoList;
    }

    private void setReportType(List<IndicatorsDto> indicatorsDtoList, ReportType reportType, int annualReportCount, int quarterlyReportCount) {
        for (int i = 0; i < annualReportCount; i++) {
            indicatorsDtoList.add(IndicatorsDto.builder()
                .issuanceCycle(IndicatorInterval.YEAR)
                .reportType(reportType)
                .build());
        }

        for (int i = 0; i < quarterlyReportCount; i++) {
            indicatorsDtoList.add(IndicatorsDto.builder()
                .issuanceCycle(IndicatorInterval.QUARTER)
                .reportType(reportType)
                .build());
        }
    }

    public List<IndicatorsDto> naverDocParsing(Document document, String ticker) {
        Elements tableElements = getTableElements(document);

        if (tableElements.size() == 0) {
            return Collections.emptyList();
        }

        Elements theadElements = getThead(tableElements);
        if (theadElements.size() == 0) {
            return Collections.emptyList();
        }
        Elements tbodyElements = getTbody(tableElements);
        if (tbodyElements.size() == 0) {
            return Collections.emptyList();
        }

        List<IndicatorsDto> indicatorsDtoList = new ArrayList<>();
        setReportType(indicatorsDtoList);

        setReportDate(indicatorsDtoList, theadElements);
        setIndicators(indicatorsDtoList, tbodyElements);
        setTicker(indicatorsDtoList, ticker);
        setReportType(indicatorsDtoList);

        return indicatorsDtoList;
    }

    private Elements getTableElements(Document document) {
        return document.select("div.section.cop_analysis div.sub_section table");
    }


    private void setReportType(List<IndicatorsDto> indicatorsDtoList) {
        for (int i = 0; i < 4; i++) {
            indicatorsDtoList.get(i).setIssuanceCycle(IndicatorInterval.YEAR);
        }

        for (int i = 4; i < indicatorsDtoList.size(); i++) {
            indicatorsDtoList.get(i).setIssuanceCycle(IndicatorInterval.QUARTER);
        }
    }

    private void setTicker(List<IndicatorsDto> indicatorsDtoList, String ticker) {
        indicatorsDtoList.forEach(indicatorsDto -> indicatorsDto.setTicker(ticker));
    }

    private void setIndicators(List<IndicatorsDto> indicatorsDtoList, Elements tbodyElements) {
        for (Element e : tbodyElements) {
            String title = e.select("th").text().split(" ")[0];
            Elements valueElements = e.select("td");

            for (int i = 0; i < indicatorsDtoList.size(); i++) {
                switch (title) {
                    case "매출액":
                        indicatorsDtoList.get(i).setTotalRevenue(parseLong(valueElements.eq(i).text()));
                        break;
                    case "영업이익":
                        indicatorsDtoList.get(i).setOperatingProfit(parseLong(valueElements.eq(i).text()));
                        break;
                    case "당기순이익":
                        indicatorsDtoList.get(i).setNetProfit(parseLong(valueElements.eq(i).text()));
                        break;
                    case "영업이익률":
                        indicatorsDtoList.get(i).setOperatingMargin(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "순이익률":
                        indicatorsDtoList.get(i).setNetMargin(parseBigDecimal(valueElements.eq(i).text()));
                        break;
//					case "ROE(지배주주)":
                    case "ROE(%)":
                        indicatorsDtoList.get(i).setRoe(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "부채비율":
                        indicatorsDtoList.get(i).setLiabilitiesRatio(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "당좌비율":
                        indicatorsDtoList.get(i).setQuickRatio(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "유보율":
                        indicatorsDtoList.get(i).setReservationRatio(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "EPS":
                    case "EPS(원)":
                        indicatorsDtoList.get(i).setEps(parseInteger(valueElements.eq(i).text()));
                        break;
                    case "PER":
                    case "PER(배)":
                        indicatorsDtoList.get(i).setPer(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "BPS":
                    case "BPS(원)":
                        indicatorsDtoList.get(i).setBps(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "PBR":
                    case "PBR(배)":
                        indicatorsDtoList.get(i).setPbr(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "주당배당금(원)":
                        indicatorsDtoList.get(i).setDividend(parseInteger(valueElements.eq(i).text()));
                        break;
                    case "배당수익률(%)":
                    case "시가배당률(%)":
                        indicatorsDtoList.get(i).setMarketPriceDividend(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "배당성향(%)":
                        indicatorsDtoList.get(i).setDividendPayoutRatio(parseBigDecimal(valueElements.eq(i).text()));
                        break;
                    case "지배주주순이익":
                        indicatorsDtoList.get(i).setNetProfitOfControllingShareHolder(parseLong(valueElements.eq(i).text()));
                        break;
                    case "비지배주주순이익":
                        indicatorsDtoList.get(i).setNetProfitOfNonControllingShareHolder(parseLong(valueElements.eq(i).text()));
                        break;
                    case "자산총계":
                        indicatorsDtoList.get(i).setTotalAssets(parseLong(valueElements.eq(i).text()));
                        break;
                    case "부채총계":
                        indicatorsDtoList.get(i).setTotalLiabilities(parseLong(valueElements.eq(i).text()));
                        break;
                    case "자본총계":
                        indicatorsDtoList.get(i).setTotalEquity(parseLong(valueElements.eq(i).text()));
                        break;
                    case "지배주주지분":
                        indicatorsDtoList.get(i).setShareOfControllingShareholder(parseLong(valueElements.eq(i).text()));
                        break;
                    case "비지배주주지분":
                        indicatorsDtoList.get(i).setShareOfNonControllingShareholder(parseLong(valueElements.eq(i).text()));
                        break;
                    case "자본금":
                        indicatorsDtoList.get(i).setCapital(parseLong(valueElements.eq(i).text()));
                        break;
                    case "지배주주순이익률":
                        indicatorsDtoList.get(i).setNetMarginOfControllingShareholder(parseLong(valueElements.eq(i).text()));
                        break;
                    case "발행주식수":
                        //todo: 추가할것
                        indicatorsDtoList.get(i).setNumberOfIssuedShares(parseLong(valueElements.eq(i).text()));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setFnGuideIssuedYeartDate(List<IndicatorsDto> indicatorsDtoList, Elements issuedYearDataElements) {
        for (int i = 0; i < indicatorsDtoList.size(); i++) {
            String text = issuedYearDataElements.eq(i).text();

            IndicatorsDto indicatorsDto = indicatorsDtoList.get(i);
            if (text.contains("(E)") || text.contains("(P)")) {
                indicatorsDto.setIndicatorType(IndicatorType.EXPECTED);
            } else {
                indicatorsDto.setIndicatorType(IndicatorType.CONFIRMED);
            }
            String[] s = text.replaceAll("[ ,():a-zA-Z가-힣]", "").split("[./]");

            if (s.length < 2) {
            } else {
                Integer businessYear = Integer.parseInt(s[0]);
                Integer businessMonth = Integer.parseInt(s[1]);
                indicatorsDto.setBusinessYear(businessYear);
                indicatorsDto.setBusinessMonth(businessMonth);
            }
        }

//		for(Element e : theadElements.select("tr").eq(1).select("th")) {
//			String[] s = e.text().replaceAll("[ ,():a-zA-Z가-힣]", "").split("[./]");
//
//			if(s.length < 2) {
//			} else {
//				String businessYear = s[0];
//				String reportCode = s[1];
//				IndicatorsDto indicatorsDto = IndicatorsDto.builder()
//					.issuedDate(businessYear)
//					.reportCode(reportCode)
//					.build();
//
//				indicatorsDtoList.add(indicatorsDto);
//			}
//		}
    }

    private void setReportDate(List<IndicatorsDto> indicatorsDtoList, Elements theadElements) {
        for (Element e : theadElements.eq(1).select("th")) {
            String[] s = e.text().replaceAll("\\(E\\)", "").split("[./]");

            if (s.length < 2) {
                indicatorsDtoList.add(IndicatorsDto.builder().build());
            } else {
                Integer businessYear = Integer.parseInt(s[0]);
                Integer reportCode = Integer.parseInt(s[1]);
                IndicatorsDto indicatorsDto = IndicatorsDto.builder()
                    .businessYear(businessYear)
                    .businessMonth(reportCode)
                    .build();

                indicatorsDtoList.add(indicatorsDto);
            }
        }
    }

    private Elements getFnGuideThead(Elements tableElements) {
        return tableElements.select("thead");
    }

    /**
     * https://opendart.fss.or.kr/api/company.json
     * https://finance.naver.com/item/main.nhn?code=
     */

    private String formatNumberString(String numberString) {
        return numberString.trim().replaceAll("(,)", "");
    }

    private Integer parseInteger(String intString) {
        if (Objects.isNull(intString) || intString.isBlank()) {
            return 0;
        }

        intString = formatNumberString(intString);

        if (intString.equals("-")) {
            return null;
        }

        return Integer.parseInt(formatNumberString(intString));
    }

    private Long parseLong(String longString) {
        if (Objects.isNull(longString) || longString.isBlank()) {
            return 0l;
        }

        longString = formatNumberString(longString);

        if (longString.equals("-")) {
            return null;
        }

        return Long.parseLong(longString);
    }

    private BigDecimal parseBigDecimal(String bigDecimalString) {
        if (Objects.isNull(bigDecimalString) || bigDecimalString.isBlank()) {
            return null;
        }

        bigDecimalString = formatNumberString(bigDecimalString);

        if (bigDecimalString.equals("-") || bigDecimalString.indexOf("N/A") > -1 || bigDecimalString.indexOf("완전잠식") > -1) {
            return null;
        }

        log.debug(bigDecimalString);
        return new BigDecimal(bigDecimalString).setScale(2, RoundingMode.HALF_EVEN);
    }

    private Elements getThead(Elements tableElements) {
        return tableElements.select("thead tr");
    }

    private Elements getTbody(Elements tableElements) {
        return tableElements.select("tbody tr");
    }
}
