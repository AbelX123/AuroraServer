package com.aurora.client.common.enumeration;

public enum Period {
    TODAY(0, "一天内"),
    YESTERDAY(1, "一天前"),
    THREE_DAY_AGO(3, "三天前"),
    SEVEN_DAY_AGO(7, "七天前"),
    HALF_MONTH_AGO(15, "半个月前"),
    MONTH_AGO(30, "一个月前"),
    THREE_MONTH_AGO(30 * 3, "三个月前"),
    SIX_MONTH_AGO(30 * 6, "半年前"),
    YEAR_AGO(30 * 12, "一年前"),
    TWO_YEAR_AGO(30 * 24, "二年前");

    private final long dayBefore;
    private final String dayName;

    Period(long dayBefore, String dayName) {
        this.dayBefore = dayBefore;
        this.dayName = dayName;
    }

    public long getDayBefore() {
        return dayBefore;
    }

    public String getDayName() {
        return dayName;
    }
}
