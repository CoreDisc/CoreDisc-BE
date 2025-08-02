package com.coredisc.presentation.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDTO {

    @Getter
    @Builder
    public static class CommentCreateRequest {
        @NotBlank(message = "댓글 내용은 필수입니다.")
        @Size(max = 500, message = "댓글은 최대 500자까지 작성 가능합니다.")
        private String content;
        private Long parentId;
    }

    private String content;
}
