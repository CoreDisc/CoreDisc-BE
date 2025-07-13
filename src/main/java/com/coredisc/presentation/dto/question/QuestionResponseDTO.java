package com.coredisc.presentation.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class QuestionResponseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class savePersonalQuestionResultDTO {

        private Long id;

        private LocalDateTime createdAt;
    }
}
