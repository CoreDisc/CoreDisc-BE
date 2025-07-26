package com.coredisc.presentation.dto.post;

import com.coredisc.domain.common.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
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
        private PublicityType publicity;

        @NotNull(message = "선택형 일기는 필수입니다.")
        private SelectiveDiaryDto selectiveDiary;


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
        @NotNull(message = "누구와 보냈는지 선택하세요.")
        private DiaryWho who;

        @NotNull(message = "장소를 선택하세요.")
        private DiaryWhere where;

        @NotNull(message = "무엇을 했는 지 선택하세요.")
        private DiaryWhat what;

        @NotNull(message = "추가정보는 필수입니다.")
        private String detail;
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

    /**
     * 게시글 조회용 request
     */

    @Getter
    @Builder
    public static class PostFeedRequestDto {

        private FeedType feedType = FeedType.ALL;
        private Long lastPostId;
        private Integer size = 10;
    }

}
