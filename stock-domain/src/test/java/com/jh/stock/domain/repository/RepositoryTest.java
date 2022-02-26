package com.jh.stock.domain.repository;

import com.jh.stock.domain.config.StockDomainDataSourceConfig;
import com.jh.stock.domain.config.StockDomainDataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({StockDomainDataSourceConfig.class, StockDomainDataSourceProperties.class})
@ContextConfiguration(classes = {
    EnableConfigurationProperties.class,
    ConfigurationPropertiesScan.class
})
@ComponentScan("com.jh.stock.domain.repository")
public class RepositoryTest {
}
