package com.jh.stock.domain;

public enum ReportType {
    UNKNOWN("몰라"),
    IFRS("IFRS(연결)");

    private String type;

    ReportType(String type) {
        this.type = type;
    }

    public static ReportType resolve(String type) {
        if ("IFRS(연결)".equals(type)) {
            return IFRS;
        } else {
            return UNKNOWN;
        }
    }
}
