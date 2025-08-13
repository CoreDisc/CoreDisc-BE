package com.coredisc.presentation.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CalendarResponseDTO {

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CalendarDTO {
        private int year;
        private int month;
        private String startDay;

        private List<DayResultDTO> days;

        private int totalDays;

        private boolean hasPrevMonth;
        private boolean hasNextMonth;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DayResultDTO{
        private int day;
        private boolean isRecorded;
        private boolean isToday;
        private Long postId;
    }
}
