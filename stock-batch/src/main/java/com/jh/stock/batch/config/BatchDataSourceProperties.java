package com.jh.stock.batch.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(JpaProperties.class)
public class BatchDataSourceProperties {
    public static final String DATA_SOURCE_NAME = "batchDataSource";
    public static final String DATA_SOURCE_PROPERTIES = "batchDataSourceProperties";

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.batch")
    public HikariConfig batchHikariConfig() {
        return new HikariConfig();
    }

    @Primary
    @Bean(name = DATA_SOURCE_NAME)
    public DataSource batchDataSource() {
        return new HikariDataSource(batchHikariConfig());
    }
}

