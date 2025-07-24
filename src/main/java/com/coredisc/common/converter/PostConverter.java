package com.coredisc.common.converter;

import com.coredisc.common.util.FileUtil;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import com.coredisc.domain.post.PostAnswerImage;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.coredisc.domain.post.QPostAnswer.postAnswer;
import static com.coredisc.domain.post.QPostAnswerImage.postAnswerImage;
import static com.coredisc.presentation.dto.post.PostResponseDTO.*;

public class PostConverter {

    private PostConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static CreatePostResultDto toCreatePostResponse(Post post,
                                                           List<TodayQuestion> todayQuestions,
                                                           LocalDate selectedDate) {

        List<TodayQuestionDto> questionDtos = todayQuestions.stream()
                .map(tq -> TodayQuestionDto.builder()
                        .questionOrder(tq.getId())
                        .type(tq.getQuestionType())
                        .isAnswered(false)
                        .build())
                .collect(Collectors.toList());

        return CreatePostResultDto.builder()
                .postId(post.getId())
                .memberId(post.getMember().getId())
                .selectedDate(selectedDate)
                .status(post.getStatus())
                .todayQuestions(questionDtos)
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static AnswerResultDto toAnswerResultDto(PostAnswer answer) {
        ImageAnswerDto imageAnswer = null;
        TextAnswerDto textAnswer = null;

        if (answer.getType() == AnswerType.IMAGE && answer.getPostAnswerImage() != null) {
            var image = answer.getPostAnswerImage();
            imageAnswer = ImageAnswerDto.builder()
                    .imageUrl(image.getImgUrl())
                    .thumbnailUrl(image.getThumbnailUrl())
                    .originalFileName(image.getOriginalFileName())
                    .build();
        }

        if (answer.getType() == AnswerType.TEXT) {
            textAnswer = TextAnswerDto.builder()
                    .content(answer.getTextContent())
                    .build();
        }

        return AnswerResultDto.builder()
                .answerId(answer.getId())
                .questionOrder(answer.getAnswerOrder())
                .answerType(answer.getType())
                .imageAnswer(imageAnswer)
                .textAnswer(textAnswer)
                .build();
    }

    /**
     * 임시저장 게시글 상세 DTO 변환
     * @param post 임시저장 게시글
     * @param answers 답변 리스트
     * @return 임시저장 게시글 상세 DTO
     */
    public static TempPostDetailDto toTempPostDetailDto(Post post, List<PostAnswer> answers) {

        //
        Map<Integer, PostAnswer> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        answer -> answer.getTodayQuestion().getQuestionOrder(), // questionOrder
                        answer -> answer
                ));

        // 1,2,3,4 순서로 답변 DTO 생성
        List<TempAnswerDto> answerDtos = IntStream.range(1, 5)
                .mapToObj(questionOrder -> {
                    PostAnswer answer = answerMap.get(questionOrder);

                    if (answer == null) {
                        // 답변이 없는 경우
                        return TempAnswerDto.builder()
                                .answerId(null)
                                .questionOrder(questionOrder)
                                .answerType(null)
                                .textContent(null)
                                .imageUrl(null)
                                .isAnswered(false)
                                .updatedAt(null)
                                .build();
                    } else {
                        // 답변이 있는 경우
                        return TempAnswerDto.builder()
                                .answerId(answer.getId())
                                .questionOrder(questionOrder)
                                .answerType(answer.getType())
                                .textContent(answer.getTextContent())
                                .imageUrl(answer.getPostAnswerImage() != null ? answer.getPostAnswerImage().getImgUrl() : null)
                                .isAnswered(true)
                                .updatedAt(answer.getUpdatedAt())
                                .build();
                    }
                })
                .collect(Collectors.toList());

        return TempPostDetailDto.builder()
                .postId(post.getId())
                .selectedDate(post.getCreatedAt().toLocalDate())
                .status(post.getStatus())
                .answers(answerDtos)
                .build();
    }


    /**
     * Post 엔티티를 PostSummary DTO로 변환 (4개 답변 포함)
     */
    public static PostFeedResponseDTO.PostSummary toPostSummary(Post post, List<PostAnswer> answers) {
        return PostFeedResponseDTO.PostSummary.builder()
                .postId(post.getId())
                .member(toMemberInfo(post))
                .selectedDate(post.getCreatedAt().toLocalDate())
                .answers(toFeedAnswerResponses(answers)) // 4개 답변 모두 포함
                .build();
    }

    /**
     * Post 엔티티를 PostDetailResponseDTO로 변환
     */
    public static PostDetailDto toPostDetailResponse(Post post, boolean isLiked) {
        return PostDetailDto.builder()
                .postId(post.getId())
                .member(toDetailMemberInfo(post))
                .selectedDate(post.getCreatedAt().toLocalDate())
                .visibility(post.getPublicity())
                .answers(toDetailAnswerResponses(post.getAnswers()))
                .selectiveDiary(toDetailSelectiveDiary(post))
                .statistics(toDetailStatistics(post))
                .isLiked(isLiked)
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

    /**
     * Member 정보를 MemberInfo DTO로 변환 (Feed용)
     */
    private static PostFeedResponseDTO.PostSummary.MemberInfo toMemberInfo(Post post) {
        return PostFeedResponseDTO.PostSummary.MemberInfo.builder()
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .profileImg(post.getMember().getProfileImg() != null ?
                        post.getMember().getProfileImg().getImgUrl() : null)
                .build();
    }

    /**
     * Member 정보를 MemberInfo DTO로 변환 (Detail용)
     */
    private static PostDetailDto.MemberInfo toDetailMemberInfo(Post post) {
        return PostDetailDto.MemberInfo.builder()
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .profileImg(post.getMember().getProfileImg() != null ?
                        post.getMember().getProfileImg().getImgUrl() : null)
                .build();
    }

    /**
     * 4개 답변을 Feed용 Answer DTO 리스트로 변환
     */
    private static List<PostFeedResponseDTO.PostSummary.Answer> toFeedAnswerResponses(List<PostAnswer> answers) {
        return answers.stream()
                .map(PostConverter::toFeedAnswerResponse)
                .collect(Collectors.toList());
    }

    /**
     * PostAnswer를 Feed용 Answer DTO로 변환
     */
    private static PostFeedResponseDTO.PostSummary.Answer toFeedAnswerResponse(PostAnswer answer) {
        return PostFeedResponseDTO.PostSummary.Answer.builder()
                .answerId(answer.getId())
                .questionContent(answer.getQuestionContent())
                .answerType(answer.getType())
                .imageAnswer(answer.getType() == AnswerType.IMAGE ?
                        toFeedImageAnswerResponse(answer.getPostAnswerImage()) : null)
                .textAnswer(answer.getType() == AnswerType.TEXT ?
                        toFeedTextAnswerResponse(answer.getTextContent()) : null)
                .build();
    }

    /**
     * PostAnswerImage를 Feed용 ImageAnswer DTO로 변환
     */
    private static PostFeedResponseDTO.PostSummary.Answer.ImageAnswer toFeedImageAnswerResponse(PostAnswerImage image) {
        if (image == null) {
            return null;
        }

        return PostFeedResponseDTO.PostSummary.Answer.ImageAnswer.builder()
                .thumbnailUrl(image.getThumbnailUrl())
                .build();
    }

    /**
     * 텍스트 답변을 Feed용 TextAnswer DTO로 변환
     */
    private static PostFeedResponseDTO.PostSummary.Answer.TextAnswer toFeedTextAnswerResponse(String textContent) {
        if (textContent == null || textContent.trim().isEmpty()) {
            return null;
        }

        return PostFeedResponseDTO.PostSummary.Answer.TextAnswer.builder()
                .content(textContent)
                .build();
    }

    /**
     * 선택형 일기를 SelectiveDiary DTO로 변환 (Detail용)
     */
    private static PostDetailDto.SelectiveDiary toDetailSelectiveDiary(Post post) {
        return PostDetailDto.SelectiveDiary.builder()
                .who(post.getDailyWho())
                .where(post.getDailyWhere())
                .what(post.getDailyWhat())
                .mood(post.getDailyDetail())
                .build();
    }

    /**
     * 통계 정보를 Statistics DTO로 변환 (Detail용)
     */
    private static PostDetailDto.Statistics toDetailStatistics(Post post) {
        return PostDetailDto.Statistics.builder()
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .viewCount(post.getViewCount())
                .build();
    }

    /**
     * PostAnswer 리스트를 Detail용 Answer DTO 리스트로 변환
     */
    private static List<PostDetailDto.Answer> toDetailAnswerResponses(List<PostAnswer> answers) {
        return answers.stream()
                .map(PostConverter::toDetailAnswerResponse)
                .collect(Collectors.toList());
    }

    /**
     * PostAnswer를 Detail용 Answer DTO로 변환
     */
    private static PostDetailDto.Answer toDetailAnswerResponse(PostAnswer answer) {
        return PostDetailDto.Answer.builder()
                .answerId(answer.getId())
                .questionContent(answer.getQuestionContent())
                .answerType(answer.getType())
                .imageAnswer(toDetailImageAnswerResponse(answer.getPostAnswerImage()))
                .textAnswer(toDetailTextAnswerResponse(answer.getTextContent()))
                .build();
    }

    /**
     * PostAnswerImage를 Detail용 ImageAnswer DTO로 변환
     */
    private static PostDetailDto.Answer.ImageAnswer toDetailImageAnswerResponse(PostAnswerImage image) {
        if (image == null) {
            return null;
        }

        return PostDetailDto.Answer.ImageAnswer.builder()
                .imageUrl(image.getImgUrl())
                .thumbnailUrl(image.getThumbnailUrl())
                .build();
    }

    /**
     * 텍스트 답변을 Detail용 TextAnswer DTO로 변환
     */
    private static PostDetailDto.Answer.TextAnswer toDetailTextAnswerResponse(String textContent) {
        if (textContent == null || textContent.trim().isEmpty()) {
            return null;
        }

        return PostDetailDto.Answer.TextAnswer.builder()
                .content(textContent)
                .build();
    }


    public static PostResponseDTO.PostFeedResponseDTO toPostFeedResponseDto(
            List<PostResponseDTO.PostFeedResponseDTO.PostSummary> posts,
            Long nextCursor,
            boolean hasNext) {
        return PostResponseDTO.PostFeedResponseDTO.builder()
                .posts(posts)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();

    }

    public static PostResponseDTO.PublishResultDto toPublishResultDto(Post post) {
        return PostResponseDTO.PublishResultDto.builder()
                .postId(post.getId())
                .status(post.getStatus())
                .publishedAt(post.getUpdatedAt())
                .build();
    }

    public static TempAnswerPostDto toTempAnswerPostDto(List<Post> tempPosts) {

        return PostResponseDTO.TempAnswerPostDto.builder()
                .PostIds(tempPosts.stream()
                        .map(Post::getId)
                        .toList())
                .build();
    }
}



