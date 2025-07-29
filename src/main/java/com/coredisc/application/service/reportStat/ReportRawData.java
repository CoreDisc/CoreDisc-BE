package com.coredisc.application.service.reportStat;

import com.coredisc.domain.reportStats.DailyRandomQuestionStat;
import com.coredisc.domain.reportStats.MonthlyFixedQuestionStat;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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
        private boolean isAllOneCount;
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
        private Map<Integer, SelectedOptionWithCount> topSelectedOption;
    }

    @Getter
    @AllArgsConstructor
    public static class SelectedOptionWithCount {
        private int selectedOption;
        private int selectionCount;
    }

    @Getter
    @AllArgsConstructor
    public static class MostSelectedQuestionItem {
        private String questionContent;
        private int selectionCount;
    }

    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class SelectionStatKey {
        private Long memberId;
        private int year;
        private int month;
        private int dailyType;
        private int selectedOption;
    }
}
