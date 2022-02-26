package com.jh.stock.domain;


import com.jh.stock.domain.converter.ReportListConverter;
import com.jh.stock.domain.converter.StringListConverter;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Table(name = "favorites")
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "json")
    private List<String> tickerList;
    @Convert(converter = ReportListConverter.class)
    private List<IndicatorReportType> indicatorReportList;

    private String slackChannel;
}
