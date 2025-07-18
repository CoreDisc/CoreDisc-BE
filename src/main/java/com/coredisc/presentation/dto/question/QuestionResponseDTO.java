package com.coredisc.presentation.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionResponseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class savePersonalQuestionResultDTO {

        private Long id;

        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class saveOfficialQuestionResultDTO {

        private Long id;

        private LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicQuestionListResultDTO {
        List<BasicQuestionResultDTO> basicQuestionList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BasicQuestionResultDTO {

        private Long id;

        private String questionType;

        private String question;
    }
}
