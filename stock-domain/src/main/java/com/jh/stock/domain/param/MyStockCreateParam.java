package com.jh.stock.domain.param;

import com.jh.stock.domain.Indicators;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MyStockCreateParam {
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
    private List<Indicators> indicatorsList;
}
