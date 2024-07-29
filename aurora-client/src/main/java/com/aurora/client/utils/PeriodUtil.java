package com.aurora.client.utils;

import com.aurora.client.common.enumeration.Period;

import java.time.LocalDateTime;

/**
 * 日期转换为距离今日多久的工具类
 */
public class PeriodUtil {

    /**
     * 给定时间算出距离今天多少天
     */
    public static Period timeToPeriod(LocalDateTime date) {
        LocalDateTime now = LocalDateTime.now();
        Period rp = Period.TODAY;

        for (Period p : Period.values()) {
            if (!date.plusDays(p.getDayBefore()).isBefore(now)) {
                break;
            }
            rp = p;
        }

        return rp;
    }

    public static String getDayNameFromDayBefore(int key) {
        for (Period p : Period.values()) {
            if (p.getDayBefore() == key) {
                return p.getDayName();
            }
        }
        return null;
    }
}
