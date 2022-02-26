package com.jh.stock.batch.job;

import com.slack.api.Slack;
import com.slack.api.webhook.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobListener extends JobExecutionListenerSupport {
    private final String WEBHOOK_URL = "https://hooks.slack.com/services/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";

    private final Map<String, String> MAP = new HashMap<>();

    {
        MAP.put("CRAWLING_INDICATORS", "재무제표 크롤링");
        MAP.put("QUOTE_JOB", "주가 데이터 업데이트");
        MAP.put("REPORT", "리포트");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        Slack slack = Slack.getInstance();
        try {
            Payload payload = Payload.builder().text("배치 이름 : " + MAP.get(jobExecution.getJobInstance().getJobName()) + System.lineSeparator() + "상태 : " + jobExecution.getStatus()).build();
            slack.send(WEBHOOK_URL, payload);

        } catch (Exception e) {
            log.error("error: {}", e.getMessage());
        }
    }
}
