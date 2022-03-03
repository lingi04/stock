package com.jh.stock.batch.job.quote;

import com.jh.stock.batch.job.quote.processor.QuoteProcessor;
import com.jh.stock.batch.job.quote.writer.QuoteWriter;
import com.jh.stock.domain.MyQuote;
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

@RequiredArgsConstructor
@Configuration
public class QuoteJobConfig {
    private static final String QUOTE_JOB = "QUOTE_JOB";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final QuoteProcessor quoteProcessor;
    private final QuoteWriter quoteWriter;
    private final JobExecutionListener jobListener;

    private final BeanFactory beanFactory;

    @Bean(name = QUOTE_JOB)
    public Job stockBatchJob() throws Exception {
        return jobBuilderFactory.get("QUOTE_JOB")
            .listener(jobListener)
            .start(quoteMasterStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    @JobScope
    public Step quoteMasterStep() throws Exception {
        return stepBuilderFactory.get("quoteMasterStep")
            .<MyStock, MyQuote>chunk(100)
            .reader(jpaPagingItemReaderQuote())
            .processor(quoteProcessor)
            .writer(quoteWriter)
            .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<MyStock> jpaPagingItemReaderQuote() {
        EntityManagerFactory stockEntityManagerFactory = (EntityManagerFactory) beanFactory.getBean("stockEntityManagerFactory");

        return new JpaPagingItemReaderBuilder<MyStock>()
            .name("jpaPagingItemReader")
            .entityManagerFactory(stockEntityManagerFactory)
            .pageSize(100)
            .queryString("SELECT s FROM MyStock s")
            .build();
    }
}
