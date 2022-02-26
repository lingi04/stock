CREATE TABLE `indicators`
(
    `id`                                         bigint unsigned NOT NULL AUTO_INCREMENT,
    `ticker`                                     varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `indicator_type`                             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT NULL,
    `business_year`                              varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `business_month`                             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `report_type`                                varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `is_expected_statements`                     tinyint(1) NOT NULL DEFAULT '0',
    `issuance_cycle`                             varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci  DEFAULT 'utf8mb4_unicode_ci',
    `total_revenue`                              int                                                           DEFAULT NULL,
    `operating_profit`                           int                                                           DEFAULT NULL,
    `net_profit`                                 int                                                           DEFAULT NULL,
    `operating_margin`                           decimal(32, 0)                                                DEFAULT NULL,
    `net_margin`                                 decimal(32, 0)                                                DEFAULT NULL,
    `roe`                                        decimal(32, 3)                                                DEFAULT NULL,
    `liabilities_ratio`                          decimal(32, 0)                                                DEFAULT NULL,
    `quick_ratio`                                decimal(32, 0)                                                DEFAULT NULL,
    `reservation_ratio`                          decimal(32, 0)                                                DEFAULT NULL,
    `eps`                                        int                                                           DEFAULT NULL,
    `per`                                        decimal(32, 0)                                                DEFAULT NULL,
    `bps`                                        decimal(32, 0)                                                DEFAULT NULL,
    `pbr`                                        decimal(32, 0)                                                DEFAULT NULL,
    `dividend`                                   decimal(32, 0)                                                DEFAULT NULL,
    `market_price_dividend`                      decimal(32, 0)                                                DEFAULT NULL,
    `dividend_payout_ratio`                      decimal(32, 0)                                                DEFAULT NULL,
    `net_profit_of_controlling_share_holder`     int                                                           DEFAULT NULL,
    `net_profit_of_non_controlling_share_holder` int                                                           DEFAULT NULL,
    `total_assets`                               int                                                           DEFAULT NULL,
    `total_liabilities`                          int                                                           DEFAULT NULL,
    `total_equity`                               int                                                           DEFAULT NULL,
    `share_of_controlling_shareholder`           int                                                           DEFAULT NULL,
    `share_of_non_controlling_shareholder`       int                                                           DEFAULT NULL,
    `capital`                                    int                                                           DEFAULT NULL,
    `net_margin_of_controlling_shareholder`      int                                                           DEFAULT NULL,
    `number_of_issued_shares`                    int                                                           DEFAULT NULL,
    `create_time`                                datetime                                                      DEFAULT NULL,
    `update_time`                                datetime                                                      DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
create table quote
(
    id                         bigint not null auto_increment,
    fluctuation                decimal(19, 2),
    market_capitalization      decimal(19, 2),
    market_price               decimal(19, 2),
    per_of_sector              decimal(19, 2),
    share_of_foreign_ownership decimal(19, 2),
    ticker                     varchar(255),
    trade_date                 datetime(6),
    transaction_price          integer,
    transaction_volume         bigint,
    year_high                  decimal(19, 2),
    year_low                   decimal(19, 2),
    primary key (id)
) engine=InnoDB;

CREATE TABLE `srim`
(
    `id`                                     bigint NOT NULL AUTO_INCREMENT,
    `excess_margin`                          decimal(19, 2)                          DEFAULT NULL,
    `required_roe`                           decimal(19, 2)                          DEFAULT NULL,
    `expected_yield`                         decimal(19, 2)                          DEFAULT NULL,
    `net_profit_of_controlling_share_holder` decimal(19, 2)                          DEFAULT NULL,
    `persistence_factor`                     decimal(19, 2)                          DEFAULT NULL,
    `pv_of_ri`                               decimal(32, 2)                          DEFAULT NULL,
    `share_of_controlling_shareholder`       decimal(19, 2)                          DEFAULT NULL,
    `ticker`                                 varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `yield_trend`                            int                                     DEFAULT NULL,
    `year`                                   varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `yield_growth`                           int                                     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
CREATE TABLE `stock`
(
    `id`                        bigint NOT NULL AUTO_INCREMENT,
    `beta`                      decimal(19, 2)                          DEFAULT NULL,
    `face_value`                decimal(19, 2)                          DEFAULT NULL,
    `name`                      varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `number_of_floating_shares` int                                     DEFAULT NULL,
    `number_of_issued_shares`   int                                     DEFAULT NULL,
    `ticker`                    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `sector`                    varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `main_products`             varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `listing_date`              date                                    DEFAULT NULL,
    `create_date`               datetime                                DEFAULT NULL,
    `update_date`               datetime                                DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
