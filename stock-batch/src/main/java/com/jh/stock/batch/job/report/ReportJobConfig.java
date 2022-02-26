package com.jh.stock.batch.job.report;

import com.jh.stock.batch.job.report.tasklet.ReportTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Configuration
public class ReportJobConfig {
    private static final String REPORT = "REPORT";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final JobExecutionListener jobListener;
    private final ReportTasklet reportTasklet;

    @Bean(name = REPORT)
    public Job stockBatchJob() throws Exception {
        return jobBuilderFactory.get(REPORT)
            .listener(jobListener)
            .start(reportMasterStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    @JobScope
    public Step reportMasterStep() throws Exception {
        return stepBuilderFactory.get("masterStep")
            .tasklet(reportTasklet)
            .build();
    }
}
