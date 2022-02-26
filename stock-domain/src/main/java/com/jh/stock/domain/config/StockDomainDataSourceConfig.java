package com.jh.stock.domain.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@ComponentScan(basePackages = "com.jh.stock.domain")
@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@EnableJpaRepositories(
    entityManagerFactoryRef = StockDomainDataSourceConfig.ENTITY_MANAGER_FACTORY_NAME,
    transactionManagerRef = StockDomainDataSourceConfig.TRANSACTION_MANAGER_NAME,
    basePackages = {"com.jh.stock.domain"}
)
@EntityScan("com.jh.stock.domain")
public class StockDomainDataSourceConfig {
    public static final String ENTITY_MANAGER_FACTORY_NAME = "stockEntityManagerFactory";
    public static final String ENTITY_MANAGER_NAME = "stockEntityManager";
    public static final String TRANSACTION_MANAGER_NAME = "stockTransactionManager";

    @Bean(name = ENTITY_MANAGER_FACTORY_NAME)
    public LocalContainerEntityManagerFactoryBean stockEntityManagerFactory(
        EntityManagerFactoryBuilder builder,
        @Qualifier(StockDomainDataSourceProperties.DATA_SOURCE_NAME) DataSource dataSource,
        JpaProperties jpaProperties,
        HibernateProperties hibernateProperties
    ) {
        return builder
            .dataSource(dataSource)
            .packages("com.jh.stock.domain")
            .persistenceUnit(ENTITY_MANAGER_NAME)
            .properties(hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(),
                new HibernateSettings()
                    .ddlAuto(() -> {
                        if (EmbeddedDatabaseConnection.isEmbedded(dataSource)) {
                            return "create-drop";
                        }
                        return "none";
                    })
            ))
            .build();
    }

    @Bean(TRANSACTION_MANAGER_NAME)
    public PlatformTransactionManager transactionManager(@Qualifier(ENTITY_MANAGER_FACTORY_NAME) LocalContainerEntityManagerFactoryBean entityManagerFactoryBean) {
        return new JpaTransactionManager(entityManagerFactoryBean.getObject());
    }
}
