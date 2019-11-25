package com.wisd.dbs.utils;

import lombok.val;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @date 2018-12-12
 * @time 13:20
 */
public class TimeUtil {
    private static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static long toMilli(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static LocalDateTime parse(String patterText) {
        return LocalDateTime.parse(patterText, formatter);
    }

    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }

    public static String plusMinutes(String patterText, int minutes) {
        return TimeUtil.format(TimeUtil.parse(patterText).plusMinutes(minutes));
    }

    public static String minusMinutes(String patterText, int minutes) {
        return TimeUtil.format(TimeUtil.parse(patterText).minusMinutes(minutes));
    }

    public static String now() {
        return format(LocalDateTime.now());
    }

    /**
     * 本周周日23：59：59
     *
     * @return
     */
    public static LocalDateTime sundayThisWeek() {
        val toDay = LocalDate.now().atTime(23, 59, 59);
        return toDay.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
    }


    /**
     * 本周一 00：00：00
     *
     * @return
     */
    public static LocalDateTime mondayThisWeek() {
        val fromDay = LocalDate.now().atTime(0, 0, 0);
        return fromDay.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    public static int week(LocalDateTime localDateTime) {
        return localDateTime.getDayOfWeek().getValue();
    }

    public static int week(String time) {
        val localDateTime = parse(time);
        return week(localDateTime);
    }
}
