package com.jh.stock.batch.job.indicators.writer;

import com.jh.stock.domain.Indicators;
import com.jh.stock.domain.service.IndicatorsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@StepScope
@Component
@RequiredArgsConstructor
public class IndicatorsWriter implements ItemWriter<List<Indicators>> {
    private final IndicatorsService indicatorsService;

    @Override
    public void write(List<? extends List<Indicators>> indicatorsLists) throws Exception {
        indicatorsLists.forEach(indicatorList -> {

            indicatorList.forEach(indicators -> {
                try {
                    indicatorsService.save(indicators);
                } catch (Exception e) {
                    log.error("티커 : {}, 발행 연도 : {}, 발행 월 : {}, 지배주주지분 : {}, 당기순이익 : {}, 발행 주식 수 : {}",
                        indicators.getTicker(), indicators.getBusinessYear(), indicators.getBusinessMonth(), indicators.getShareOfControllingShareholder(), indicators.getNetProfit(), indicators.getNumberOfIssuedShares());
                    throw e;
                }
            });
        });
    }
}
