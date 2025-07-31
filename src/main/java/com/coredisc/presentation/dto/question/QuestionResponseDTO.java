package com.coredisc.presentation.dto.question;

import com.coredisc.domain.common.enums.QuestionType;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BasicQuestionResultDTO {

        private Long id;

        private String questionType;

        private String question;

        private LocalDateTime createdAt;
    }

//    @Builder
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class MySharedQuestionListResultDTO {
//        private Long mySharedQuestionCnt;
//        private CursorDTO<QuestionResponseDTO.MySharedQuestionResultDTO> mySharedQuestionList;
//    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MySharedQuestionResultDTO {

        private Long id;

        private List<CategoryResponseDTO.CategoryInfoDTO> categories;

        private String question;

        private Long sharedCount;

        private Boolean isFavorite;

        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SaveFixedTodayQuestionResultDTO {

        private Long id;

        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SaveRandomTodayQuestionResultDTO {

        private Long id;

        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SelectedTodayQuestionResultDTO {

        private Long id;

        private Integer questionOrder;

        private String question;

        private QuestionType questionType;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SaveMemberOfficialQuestionResultDTO {

        private Long id;

        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SavedSharedQuestionResultDTO {

        private Long id;

        private List<CategoryResponseDTO.CategoryInfoDTO> categories;

        private String question;

        private Long sharedCount;

        private Boolean isFavorite;

        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PopularQuestionListResultDTO {

        private LocalDate startDate;

        private LocalDate endDate;

        private List<PopularQuestionResultDTO> popularQuestionList;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PopularQuestionResultDTO {

        private Long id;

        private String username;

        private String question;

        private Long sharedCount;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateSavedSharedQuestionFavoriteStatusResultDTO {

        private Long id;

        private LocalDateTime createdAt;
    }

}
