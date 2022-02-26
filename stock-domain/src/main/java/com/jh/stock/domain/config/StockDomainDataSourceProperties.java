package com.jh.stock.domain.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(JpaProperties.class)
public class StockDomainDataSourceProperties {
    public static final String DATA_SOURCE_NAME = "stockDataSource";
    public static final String DATA_SOURCE_PROPERTIES = "stockDataSourceProperties";

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.stock")
    public HikariConfig stockHikariConfig() {
        return new HikariConfig();
    }

    @Bean(name = DATA_SOURCE_NAME)
    public DataSource stockDataSource() {
        return new HikariDataSource(stockHikariConfig());
    }
}

