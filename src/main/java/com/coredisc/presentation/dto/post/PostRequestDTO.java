package com.coredisc.presentation.dto.post;

import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.common.enums.PublicityType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class PostRequestDTO {

    @Getter
    @NoArgsConstructor
    public static class CreatePostDto {
        @NotNull(message = "선택 날짜는 필수입니다.")
        @PastOrPresent(message = "미래 날짜는 선택할 수 없습니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate selectedDate;
    }

    @Getter
    @NoArgsConstructor
    public static class PublishPostDto {
        @NotNull(message = "공개 범위는 필수입니다.")
        private PublicityType visibility;

        @NotNull(message = "선택형 일기는 필수입니다.")
        private SelectiveDiaryDto selectiveDiary;

        private String diaryContent;


        @Getter
        @NoArgsConstructor
        public static class SelectiveDiaryDto {
            @NotNull(message = "누구와는 필수입니다.")
            private String who;

            @NotNull(message = "어디서는 필수입니다.")
            private String where;

            @NotNull(message = "무엇을은 필수입니다.")
            private String what;

            @NotNull(message = "기분은 필수입니다.")
            private String mood;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class TextAnswerDto {

        @NotNull(message = "답변 작성은 필수입니다.")
        private String content; // TEXT 타입일 때만 적용
    }

    @Getter
    @NoArgsConstructor
    public static class SelectiveDiaryDto {
        private String who;
        private String where;
        private String what;
        private String mood;
    }

    /**
     * Image 업로드
     */

    public static class ImageAnswerDto {
        private MultipartFile image;

        public ImageAnswerDto(MultipartFile image) {
            this.image = image;
        }

        public MultipartFile getImage() {
            return image;
        }
    }


}
