package com.coredisc.presentation.dto.post;

import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.common.enums.PublicityType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {
    @Getter
    @Builder
    public static class CreatePostResultDto {
        private Long postId;
        private Long memberId;
        private String selectedDate;
        private PostStatus status;
        private List<TodayQuestionDto> todayQuestions;
        private LocalDateTime createdAt;

        /**
         * 임시.. today_question_dto
         */
        @Getter
        @Builder
        public static class TodayQuestionDto {
            private Integer type;
            private Long questionId;
            private String questionType;
            private String content;
        }
    }

    @Getter
    @Builder
    public static class PublishResultDto {
        private Long postId;
        private PostStatus status;
        private LocalDateTime publishedAt;
    }

    @Getter
    @Builder
    public static class PostDetailDto {
        private Long postId;
        private MemberDto member;
        private String selectedDate;
        private PostStatus status;
        private PublicityType visibility;
        private List<AnswerDto> answers;
        private SelectiveDiaryDto selectiveDiary;
        private String diaryContent;
        private StatisticsDto statistics;
        private Boolean isLiked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        @Getter
        @Builder
        public static class MemberDto {
            private Long memberId;
            private String nickname;
            private String profileImg;
        }

        @Getter
        @Builder
        public static class AnswerDto {
            private Long answerId;
            private Integer questionType;
            private QuestionDto question;
            private AnswerType answerType;
            private ImageAnswerDto imageAnswer;
            private TextAnswerDto textAnswer;

            @Getter
            @Builder
            public static class QuestionDto {
                private Long questionId;
                private String type;
                private String content;
            }

            @Getter
            @Builder
            public static class ImageAnswerDto {
                private String imageUrl;
                private String thumbnailUrl;
            }

            @Getter
            @Builder
            public static class TextAnswerDto {
                private String content;
            }
        }

        @Getter
        @Builder
        public static class SelectiveDiaryDto {
            private String who;
            private String where;
            private String what;
            private String mood;
        }

        @Getter
        @Builder
        public static class StatisticsDto {
            private Integer likeCount;
            private Integer commentCount;
            private Integer viewCount;
        }
    }

    @Getter
    @Builder
    public static class PostListDto {
        private List<PostSummaryDto> content;
        private PageDto page;

        @Getter
        @Builder
        public static class PostSummaryDto {
            private Long postId;
            private PostDetailDto.MemberDto member;
            private String selectedDate;
            private String previewImage;
            private PostDetailDto.SelectiveDiaryDto selectiveDiary;
            private PostDetailDto.StatisticsDto statistics;
            private LocalDateTime createdAt;
        }

        @Getter
        @Builder
        public static class PageDto {
            private Integer number;
            private Integer size;
            private Long totalElements;
            private Integer totalPages;
        }
    }

    @Getter
    @Builder
    public static class AnswerResultDto {
        private Long answerId;
        private Integer questionType;
        private AnswerType answerType;
        private PostDetailDto.AnswerDto.ImageAnswerDto imageAnswer;
        private PostDetailDto.AnswerDto.TextAnswerDto textAnswer;
    }

    @Getter
    @Builder
    public static class UploadUrlDto {
        private String uploadUrl;
        private String imageUrl;
        private LocalDateTime expiresAt;
    }
}
