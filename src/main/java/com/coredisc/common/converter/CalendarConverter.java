package com.coredisc.common.converter;

import com.coredisc.presentation.dto.calendar.CalendarResponseDTO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalendarConverter {

    private CalendarConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<CalendarResponseDTO.DayResultDTO> toDayResultDTO (int year, int month, Map<Integer, Long> dayToPostIdMap) {
        int lastDay = YearMonth.of(year, month).lengthOfMonth();

        List<CalendarResponseDTO.DayResultDTO> days = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int day = 1; day <= lastDay; day++) {
            boolean isToday = today.getYear() == year
                    && today.getMonthValue() == month
                    && today.getDayOfMonth() == day;

            Long postId = dayToPostIdMap.get(day);

            days.add(CalendarResponseDTO.DayResultDTO.builder()
                    .day(day)
                    .isRecorded(postId != null)
                    .isToday(isToday)
                    .postId(postId)
                    .build());
        }

        return days;
    }

    public static CalendarResponseDTO.CalendarDTO toCalendarDTO(int year, int month, List<CalendarResponseDTO.DayResultDTO> days, int totalDays, int continuesDays, LocalDate signupDate) {
        YearMonth target = YearMonth.of(year, month);
        boolean hasPrevMonth = target.isAfter(YearMonth.from(signupDate));
        boolean hasNextMonth = target.isBefore(YearMonth.now());

        String startDay = target.atDay(1).getDayOfWeek().name().toLowerCase();

        return CalendarResponseDTO.CalendarDTO.builder()
                .year(year)
                .month(month)
                .startDay(startDay)
                .days(days)
                .totalDays(totalDays)
                .continuesDays(continuesDays)
                .hasPrevMonth(hasPrevMonth)
                .hasNextMonth(hasNextMonth)
                .build();
    }
}
