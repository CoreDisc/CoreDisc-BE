package com.coredisc.application.service.report;

import com.coredisc.domain.stats.DailyRandomQuestionStat;
import com.coredisc.domain.stats.MonthlyFixedQuestionStat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportRawData {

    @Getter
    @AllArgsConstructor
    public static class HourlyAnswerRawData {
        private int year;
        private int month;
        private Map<Integer, Integer> hourCountMap;
    }

    @Getter
    @AllArgsConstructor
    public static class MostSelectedQuestionRawData {
        private int year;
        private int month;
        private List<MostSelectedQuestionItem> questions;
    }

    @Getter
    @AllArgsConstructor
    public static class QuestionListRawData {
        private int year;
        private int month;
        private List<MonthlyFixedQuestionStat> fixedQuestions;
        private List<DailyRandomQuestionStat> randomQuestions;
    }

    @Getter
    @AllArgsConstructor
    public static class DailyOptionRawData {
        private int year;
        private int month;
        private int dailyType;
        private String optionContent;
        private int selectionCount;
    }

    @Getter
    @AllArgsConstructor
    public static class DailyDetailRawData {
        private int year;
        private int month;
        private Map<LocalDate, String> dailyDetails;
    }

    @Getter
    @AllArgsConstructor
    public static class MostSelectedQuestionItem {
        private Long questionId;
        private String questionContent;
        private int selectionCount; // 집계된 선택 횟수
    }
}
