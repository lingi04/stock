package com.jh.stock.batch.job.indicators;

import com.jh.stock.batch.job.indicators.processor.IndicatorsProcessor;
import com.jh.stock.batch.job.indicators.writer.IndicatorsWriter;
import com.jh.stock.domain.Indicators;
import com.jh.stock.domain.MyStock;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class IndicatorsJobConfig {
    private static final String CRAWLING_INDICATORS = "CRAWLING_INDICATORS";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final IndicatorsProcessor indicatorsProcessor;
    private final IndicatorsWriter indicatorsWriter;
    private final JobExecutionListener jobListener;

    private final BeanFactory beanFactory;

    @Bean(name = CRAWLING_INDICATORS)
    public Job stockBatchJob() throws Exception {
        return jobBuilderFactory.get(CRAWLING_INDICATORS)
            .listener(jobListener)
            .start(masterStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    @JobScope
    public Step masterStep() throws Exception {
        return stepBuilderFactory.get("masterStep")
            .<MyStock, List<Indicators>>chunk(200)
            .reader(jpaPagingItemReader())
            .processor(indicatorsProcessor)
            .writer(indicatorsWriter)
            .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<MyStock> jpaPagingItemReader() {
        EntityManagerFactory stockEntityManagerFactory = (EntityManagerFactory) beanFactory.getBean("stockEntityManagerFactory");

        return new JpaPagingItemReaderBuilder<MyStock>()
            .name("jpaPagingItemReader")
            .entityManagerFactory(stockEntityManagerFactory)
            .pageSize(100)
            .queryString("SELECT s FROM MyStock s where s.ticker = '005930'")
//            .queryString("SELECT s FROM MyStock s")
            .build();
    }
}
