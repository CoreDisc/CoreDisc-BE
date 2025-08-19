package com.coredisc.presentation.dto.reportStat;

import com.coredisc.domain.common.enums.TimeZoneType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportStatResponseDTO {

    @JsonPropertyOrder({
            "year", "month", "fixedQuestions", "randomQuestions", "allOneCount", "mostSelectedQuestions", "peakTimeZone", "hasPreviousReport", "hasNextReport"
    })
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MonthlyReportDTO{ // 월별 리포트 - 기본 화면
        private int year;
        private int month;
        private List<QuestionDTO> fixedQuestions;
        private List<QuestionDTO> randomQuestions;
        private boolean isAllOneCount;
        private List<SelectedQuestionDTO> mostSelectedQuestions;
        private TimeZoneType peakTimeZone;
        private boolean hasPreviousReport;
        private boolean hasNextReport;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopDailySelectionDTO{ //선택형 일기 최다 선택 옵션
        private int year;
        private int month;
        private List<DailyOptionDTO> dailyList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class DailyDetailListDTO{
        private Map<LocalDate, String> dailyDetails;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDTO{ //질문
        private String questionContent;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectedQuestionDTO{ //선택된 질문
        private String questionContent;
        private int selectedCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyOptionDTO{ //선택형 일기 옵션
        private String dailyType;
        private String optionContent;
        private int selectionCount;
    }
}