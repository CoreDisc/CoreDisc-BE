package com.coredisc.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    public static LocalDate getStartDate(int year, int month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate getEndDate(int year, int month) {
        return LocalDate.of(year, month, 1)
                .withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth());
    }

    public static LocalDateTime getStartDateTime(int year, int month) {
        return getStartDate(year, month).atStartOfDay(); // 00:00:00
    }

    public static LocalDateTime getEndDateTime(int year, int month) {
        return getEndDate(year, month).atTime(23, 59, 59); // 23:59:59
    }

    public static String getWeekdayShort(LocalDateTime dateTime) {
        return dateTime.getDayOfWeek().toString().substring(0, 3);
    }

    public static String getYYMMDD(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
        return dateTime.format(formatter);
    }
}
