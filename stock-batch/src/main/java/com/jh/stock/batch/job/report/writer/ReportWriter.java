package com.jh.stock.batch.job.report.writer;

import com.jh.stock.domain.Indicators;
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
public class ReportWriter implements ItemWriter<Indicators> {

    @Override
    public void write(List<? extends Indicators> indicatorList) throws Exception {
        indicatorList.forEach(indicators -> {
            log.info(indicators.getTicker());
        });
    }
}
