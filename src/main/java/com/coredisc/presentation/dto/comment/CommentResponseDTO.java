package com.coredisc.presentation.dto.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class CommentResponseDTO {

    @Getter
    @Builder
    public static class CommentListResponse {
        private List<CommentResponse> comments;
        private PageInfo page;

        @Getter
        @Builder
        public static class PageInfo {
            private int number;
            private int size;
            private long totalElements;
            private int totalPages;
            private boolean hasNext;
        }
    }


    @Getter
    @Builder
    public static class CommentResponse {
        private Long commentId;
        private String content;
        private Integer depth;
        private Long parentId;
        private MemberInfo member;
        private LocalDateTime createdAt;
        private List<CommentResponse> replies;

    }

    @Getter
    @Builder
    public static class CommentCreateResponse {
        private Long commentId;
        private Long postId;
        private String content;
        private Long parentId;
        private Integer depth;
        private MemberInfo member;

    }

    @Getter
    @Builder
    public static class MemberInfo {
        private Long memberId;
        private String nickname;
        private String profileImg;

    }

    @Getter
    @Builder
    public static class CommentUpdateResponse {
        private Long commentId;
        private String content;
        private LocalDateTime updatedAt;
    }

}
