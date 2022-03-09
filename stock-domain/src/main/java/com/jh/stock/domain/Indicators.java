package com.jh.stock.domain;

import com.jh.stock.domain.exception.AlreadyInitializedException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Slf4j
@Getter
@Entity
@Table(name = "indicators")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "indicatorType")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public abstract class Indicators {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    protected MyStock myStock;

    protected String ticker;


    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false)
    protected IndicatorType indicatorType;

    protected Integer businessYear;

    protected Integer businessMonth;

    //IFRS
    @Enumerated(EnumType.STRING)
    protected ReportType reportType;

    protected boolean isExpectedStatements;

    @Enumerated(EnumType.STRING)
    protected IndicatorInterval issuanceCycle;

    /**
     * 지표(Indicators)
     */
    //@Column(columnDefinition = "comment = '매출액'")
    protected Long totalRevenue;
    ////@Colum(columnDefinition = "comment = '영업이익'")
    protected Long operatingProfit;
    //@Colum(columnDefinition = "comment = '순이익'")
    protected Long netProfit;
    //@Colum(columnDefinition = "comment = '영업이익률'")
    protected BigDecimal operatingMargin;
    //@Colum(columnDefinition = "comment = '순이익률'")
    protected BigDecimal netMargin;
    //@Colum(columnDefinition = "comment = 'ROE'")
    protected BigDecimal roe;
    //@Colum(columnDefinition = "comment = '부채비율'")
    protected BigDecimal liabilitiesRatio;
    //@Colum(columnDefinition = "comment = '당좌비율'")
    protected BigDecimal quickRatio;
    //@Colum(columnDefinition = "comment = '유보율'")
    protected BigDecimal reservationRatio;
    //@Colum(columnDefinition = "comment = 'eps'")
    protected Integer eps;
    //@Colum(columnDefinition = "comment = 'per'")
    protected BigDecimal per;
    //@Colum(columnDefinition = "comment = 'bps'")
    protected BigDecimal bps;
    //@Colum(columnDefinition = "comment = 'pbr'")
    protected BigDecimal pbr;
    //@Colum(columnDefinition = "comment = '배당금'")
    protected Integer dividend;
    //@Colum(columnDefinition = "comment = '시가배당률'")
    protected BigDecimal marketPriceDividend;
    //@Colum(columnDefinition = "comment = '배당성향'")
    protected BigDecimal dividendPayoutRatio;
    //@Colum(columnDefinition = "comment = '지배 주주 순이익'")
    protected Long netProfitOfControllingShareHolder;
    //@Colum(columnDefinition = "comment = '비 지배 주주 순이익'")
    protected Long netProfitOfNonControllingShareHolder;
    //@Colum(columnDefinition = "comment = '자산 총계'")
    protected Long totalAssets;
    //@Colum(columnDefinition = "comment = '부채 총계'")
    protected Long totalLiabilities;
    //@Colum(columnDefinition = "comment = '자본 총계'")
    protected Long totalEquity;
    //@Colum(columnDefinition = "comment = '지배주주 지분'")
    protected Long shareOfControllingShareholder;
    //@Colum(columnDefinition = "comment = '비지배주주 지분'")
    protected Long shareOfNonControllingShareholder;
    //@Colum(columnDefinition = "comment = '자본금'")
    protected Long capital;
    //@Colum(columnDefinition = "comment = '지배주주 순이익률'")
    protected Long netMarginOfControllingShareholder;
    //@Colum(columnDefinition = "comment = '발행 주식 수'")
    protected Long numberOfIssuedShares;


    @CreationTimestamp
    private Timestamp createTime;

    @UpdateTimestamp
    private Timestamp updateTime;

    public boolean isExpected() {
        if (IndicatorType.EXPECTED == indicatorType) {
            return true;
        }

        return false;
    }

    public boolean isConfirmed() {
        return !isExpected();
    }

//    public void update(IndicatorsParam indicatorsParam) {
//        this.businessYear = Optional.ofNullable(indicatorsParam.getBusinessYear()).orElse(businessYear);
//        this.businessMonth = Optional.ofNullable(indicatorsParam.getBusinessMonth()).orElse(this.businessMonth);
//        this.issuanceCycle = Optional.ofNullable(indicatorsParam.getIssuanceCycle()).orElse(this.issuanceCycle);
//        this.ticker = Optional.ofNullable(indicatorsParam.getTicker()).orElse(this.ticker);
//        this.totalRevenue = Optional.ofNullable(indicatorsParam.getTotalRevenue()).orElse(this.totalRevenue);
//        this.operatingMargin = Optional.ofNullable(indicatorsParam.getOperatingMargin()).orElse(this.operatingMargin);
//        this.netProfit = Optional.ofNullable(indicatorsParam.getNetProfit()).orElse(this.netProfit);
//        this.operatingMargin = Optional.ofNullable(indicatorsParam.getOperatingMargin()).orElse(this.operatingMargin);
//        this.netMargin = Optional.ofNullable(indicatorsParam.getNetMargin()).orElse(this.netMargin);
//        this.roe = Optional.ofNullable(indicatorsParam.getRoe()).orElse(this.roe);
//        this.liabilitiesRatio = Optional.ofNullable(indicatorsParam.getLiabilitiesRatio()).orElse(this.liabilitiesRatio);
//        this.quickRatio = Optional.ofNullable(indicatorsParam.getQuickRatio()).orElse(this.quickRatio);
//        this.reservationRatio = Optional.ofNullable(indicatorsParam.getReservationRatio()).orElse(this.reservationRatio);
//        this.eps = Optional.ofNullable(indicatorsParam.getEps()).orElse(this.eps);
//        this.per = Optional.ofNullable(indicatorsParam.getPer()).orElse(this.per);
//        this.bps = Optional.ofNullable(indicatorsParam.getBps()).orElse(this.bps);
//        this.pbr = Optional.ofNullable(indicatorsParam.getPbr()).orElse(this.pbr);
//        this.dividend = Optional.ofNullable(indicatorsParam.getDividend()).orElse(this.dividend);
//        this.marketPriceDividend = Optional.ofNullable(indicatorsParam.getMarketPriceDividend()).orElse(this.marketPriceDividend);
//        this.dividendPayoutRatio = Optional.ofNullable(indicatorsParam.getDividendPayoutRatio()).orElse(this.dividendPayoutRatio);
//        this.netProfitOfControllingShareHolder = Optional.ofNullable(indicatorsParam.getNetProfitOfControllingShareHolder()).orElse(this.netProfitOfControllingShareHolder);
//        this.netProfitOfNonControllingShareHolder = Optional.ofNullable(indicatorsParam.getNetProfitOfNonControllingShareHolder()).orElse(this.netProfitOfNonControllingShareHolder);
//        this.totalAssets = Optional.ofNullable(indicatorsParam.getTotalAssets()).orElse(this.totalAssets);
//        this.totalLiabilities = Optional.ofNullable(indicatorsParam.getTotalLiabilities()).orElse(this.totalLiabilities);
//        this.totalEquity = Optional.ofNullable(indicatorsParam.getTotalEquity()).orElse(this.totalEquity);
//        this.shareOfControllingShareholder = Optional.ofNullable(indicatorsParam.getShareOfControllingShareholder()).orElse(this.shareOfControllingShareholder);
//        this.shareOfNonControllingShareholder = Optional.ofNullable(indicatorsParam.getShareOfNonControllingShareholder()).orElse(this.shareOfNonControllingShareholder);
//        this.capital = Optional.ofNullable(indicatorsParam.getCapital()).orElse(this.capital);
//        this.netMarginOfControllingShareholder = Optional.ofNullable(indicatorsParam.getNetMarginOfControllingShareholder()).orElse(this.netMarginOfControllingShareholder);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, businessYear, businessMonth, indicatorType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Indicators) {


            Indicators indicators = (Indicators) obj;
            return (indicatorType == indicators.getIndicatorType() || this.indicatorType.equals(indicators.getIndicatorType()))
                && (this.businessYear == indicators.getBusinessYear() || this.businessYear.equals(indicators.getBusinessYear()))
                && (this.businessMonth == indicators.getBusinessMonth() || this.businessMonth.equals(indicators.getBusinessMonth()))
                && (this.reportType == indicators.getReportType() || this.reportType.equals(indicators.getReportType()))
                && (this.issuanceCycle == indicators.getIssuanceCycle() || this.issuanceCycle.equals(indicators.getIssuanceCycle()))
                && (this.ticker == indicators.getTicker() || this.ticker.equals(indicators.getTicker()))
                && compareLong(this.totalRevenue, indicators.getTotalRevenue())
                && compareLong(this.operatingProfit, indicators.getOperatingProfit())
                && compareLong(this.netProfit, indicators.getNetProfit())
                && compareBigDecimal(this.operatingMargin, indicators.getOperatingMargin())
                && compareBigDecimal(this.netMargin, indicators.getNetMargin())
                && compareBigDecimal(this.roe, indicators.getRoe())
                && compareBigDecimal(this.liabilitiesRatio, indicators.getLiabilitiesRatio())
                && compareBigDecimal(this.quickRatio, indicators.getQuickRatio())
                && compareBigDecimal(this.reservationRatio, indicators.getReservationRatio())
                && compareInt(this.eps, indicators.getEps())
                && compareBigDecimal(this.per, indicators.getPer())
                && compareBigDecimal(this.bps, indicators.getBps())
                && compareBigDecimal(this.pbr, indicators.getPbr())
                && compareInt(this.dividend, indicators.getDividend())
                && compareBigDecimal(this.marketPriceDividend, indicators.getMarketPriceDividend())
                && compareBigDecimal(this.dividendPayoutRatio, indicators.getDividendPayoutRatio())
                && compareLong(this.netProfitOfControllingShareHolder, indicators.getNetProfitOfControllingShareHolder())
                && compareLong(this.netProfitOfNonControllingShareHolder, indicators.getNetProfitOfNonControllingShareHolder())
                && compareLong(this.totalAssets, indicators.getTotalAssets())
                && compareLong(this.totalLiabilities, indicators.getTotalLiabilities())
                && compareLong(this.totalEquity, indicators.getTotalEquity())
                && compareLong(this.shareOfControllingShareholder, indicators.getShareOfControllingShareholder())
                && compareLong(this.shareOfNonControllingShareholder, indicators.getShareOfNonControllingShareholder())
                && compareLong(this.capital, indicators.getCapital())
                && compareLong(this.netMarginOfControllingShareholder, indicators.getNetMarginOfControllingShareholder());
        }

        return false;
    }

    private boolean compareLong(Long now, Long next) {
        return compare(now, next);
    }

    private boolean compareInt(Integer now, Integer next) {
        return compare(now, next);
    }

    private boolean compareBigDecimal(BigDecimal now, BigDecimal next) {
        return compare(now, next);
    }

    private boolean compare(Object o1, Object o2) {
        if (o1 == null && o2 == null) {
            return true;
        } else if (o1 == null || o2 == null) {
            return false;
        } else {
            return o1.equals(o2);
        }
    }

    public void initializeStocks(@NonNull MyStock myStock) {
        if (this.myStock != null) {
            throw new AlreadyInitializedException("myStock");
        }

        this.myStock = myStock;
    }
}
