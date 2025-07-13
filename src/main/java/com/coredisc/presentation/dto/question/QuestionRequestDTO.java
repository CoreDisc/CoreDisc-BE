package com.coredisc.presentation.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

public class QuestionRequestDTO {

    @Getter
    public static class SavePersonalQuestionDTO {

        @NotNull
        @Size(min = 1, max = 3, message = "카테고리는 최소 1개, 최대 3개까지 선택할 수 있습니다.")
        @Schema(description = "categoryId 목록", example = "[1, 2]")
        List<Long> categoryIdList;

        @NotBlank(message = "질문 입력은 필수입니다.")
        @Size(max = 50, message = "질문은 50자 이내로 입력해주세요.")
        @Schema(description = "question", example = "오늘 아침에 먹은 것은 무엇인가요?")
        String question;
    }
}
