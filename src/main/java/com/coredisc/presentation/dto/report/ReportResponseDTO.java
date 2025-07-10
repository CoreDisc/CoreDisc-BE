package com.coredisc.presentation.dto.report;

import com.coredisc.domain.common.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ReportResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeakHourResultDTO{ //최다 답변 시간대
        private int year;
        private int month;
        private List<HourlyAnswerCountDTO> topHours;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MostSelectedQuestionDTO{ //최다 선택한 랜덤 질문
        private int year;
        private int month;
        private List<QuestionDTO> questions;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionListDTO{ //월별 전체 질문 리스트
        private int year;
        private int month;
        private List<QuestionDTO> fixedQuestions;
        private List<QuestionDTO> randomQuestions;
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
    public static class HourlyAnswerCountDTO{ //시간대 별 응답수
        private int hour;
        private int answerCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionDTO{ //질문
        private Long id;
        private QuestionType questionType;
        private String questionContent;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyOptionDTO{ //선택형 일기 옵션
        private String OptionContent;
        private int selectionCount;
    }
}