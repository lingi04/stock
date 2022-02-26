package com.jh.stock.batch.job.report.processor;

import com.jh.stock.domain.Indicators;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportProcessor implements ItemProcessor<Indicators, Indicators> {
    @Override
    public Indicators process(Indicators item) throws Exception {
        return item;
    }
}
