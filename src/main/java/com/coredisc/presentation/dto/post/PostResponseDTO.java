package com.coredisc.presentation.dto.post;

import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.common.enums.PublicityType;
import com.coredisc.domain.common.enums.QuestionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDTO {
    @Getter
    @Builder
    public static class CreatePostResultDto {
        private Long postId;
        private Long memberId;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate selectedDate;

        private PostStatus status; //TEMP
        private List<TodayQuestionDto> todayQuestions;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        private LocalDateTime createdAt;


    }
    /**
     * 임시.. today_question_dto
     */
    @Getter
    @Builder
    public static class TodayQuestionDto {
        private Long questionOrder;            // today_question의 id 0,1,2,3
        private QuestionType type;        // 0: 고정, 1: 랜덤
        private String publicity; // PERSONAL 또는 OFFICIAL
        private String content;      // 질문 내용 ->
        private Boolean isAnswered; //답변 완료 여부
        private AnswerType answerType; // 답변 타입 (있는 경우)
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
    public static class ImageAnswerDto {
        private String imageUrl;
        private String thumbnailUrl;
        private String originalFileName;
        private Long fileSize;
        private String fileSizeFormatted;
        private Boolean hasThumbnail;
    }

    @Getter
    @Builder
    public static class TextAnswerDto {
        private String content;
        private Integer characterCount;

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
        private Integer questionId;
        private AnswerType answerType;
        private ImageAnswerDto imageAnswer;
        private TextAnswerDto textAnswer;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class UploadUrlDto {
        private String uploadUrl;
        private String imageUrl;
        private LocalDateTime expiresAt;
    }
}
