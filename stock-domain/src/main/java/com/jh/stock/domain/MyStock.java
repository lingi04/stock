package com.jh.stock.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Getter
@Entity
@Builder
@Table(name = "stock")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MyStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String ticker;
    // 유동주식수 / 비율
    private Integer numberOfFloatingShares;
    //발행 주식 수
    private Integer numberOfIssuedShares;
    // 액면가
    private BigDecimal faceValue;
    // 베타
    private BigDecimal beta;

    @OneToMany
    @JoinColumn(name = "stock_id")
    private List<Indicators> indicatorsList;

    @Transient
    private List<MyQuote> quoteList;
}
