package com.jh.stock.domain.repository;

import com.jh.stock.domain.MyQuote;
import com.jh.stock.domain.config.QuerydslConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({QuerydslConfig.class})
@ContextConfiguration(classes = {
    EnableConfigurationProperties.class,
    ConfigurationPropertiesScan.class
})
@ComponentScan("com.jh.stock.domain.config")
public class MyQuoteRepositoryTest {
    @Autowired
    MyQuoteRepository myQuoteRepository;

    @BeforeEach
    public void setup() {
        myQuoteRepository.deleteAll();
        myQuoteRepository.saveAll(getData());
    }

    private List<MyQuote> getData() {
        return List.of(
            MyQuote.builder()
                .id(1)
                .ticker("1")
                .tradeDate(Timestamp.valueOf("2022-02-19 12:00:00"))
                .build(),
            MyQuote.builder()
                .id(2)
                .ticker("1")
                .tradeDate(Timestamp.valueOf("2022-02-18 12:00:00"))
                .build(),
            MyQuote.builder()
                .id(3)
                .ticker("2")
                .tradeDate(Timestamp.valueOf("2022-02-17 12:00:00"))
                .build(),
            MyQuote.builder()
                .id(4)
                .ticker("2")
                .tradeDate(Timestamp.valueOf("2022-02-18 12:00:00"))
                .build()
        );
    }

    @Test
    public void getLatestQuoteBy_test() {
        List<MyQuote> quoteList = myQuoteRepository.getLatestQuoteBy(Set.of("1", "2"));

        Map<String, MyQuote> map = quoteList.stream().collect(Collectors.toMap(MyQuote::getTicker, Function.identity()));

        MyQuote expected1 = MyQuote.builder()
            .id(1)
            .ticker("1")
            .tradeDate(Timestamp.valueOf("2022-02-19 12:00:00"))
            .build();
        MyQuote expected2 = MyQuote.builder()
            .id(4)
            .ticker("2")
            .tradeDate(Timestamp.valueOf("2022-02-18 12:00:00"))
            .build();

        assertThat(map.get("1"))
            .usingRecursiveComparison()
            .isEqualTo(expected1);
        assertThat(map.get("2"))
            .usingRecursiveComparison()
            .isEqualTo(expected2);
    }

}
