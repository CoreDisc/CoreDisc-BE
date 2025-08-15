package com.coredisc.presentation.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
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
        private MemberInfo member;
        private String content;
        private Long parentId;
        private Integer depth;
        private LocalDateTime createdAt;
        private List<CommentCreateResponse> replies;

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
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private boolean hasReplies;
        // 댓글 작성 시각 표시 ("방금 전", "5분 전", "2시간 전", "3일 전" 등)
        private String timeStamp;
        // 답글 수
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer replyCount;

    }

    @Getter
    @Builder
    public static class MemberInfo {
        private Long memberId;
        private String username;
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
