package com.coredisc.common.converter;

import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import com.coredisc.domain.post.PostAnswerImage;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.coredisc.domain.post.QPostAnswer.postAnswer;
import static com.coredisc.domain.post.QPostAnswerImage.postAnswerImage;
import static com.coredisc.presentation.dto.post.PostResponseDTO.*;

@Slf4j
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
                        PostAnswer::getAnswerOrder, // questionOrder
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
                .status(post.getStatus())
                .answers(answerDtos)
                .build();
    }


    /**
     * Post 엔티티를 PostSummary DTO로 변환 (1개 답변만)
     */
    public static PostFeedResponseDTO.PostSummary toPostSummary(Post post, PostAnswer answer, String question) {


        return PostFeedResponseDTO.PostSummary.builder()
                .postId(post.getId())
                .member(toMemberInfo(post))
                .publicity(post.getPublicity())
                .selectedDate(post.getCreatedAt().toLocalDate())
                .answer(toFeedAnswerResponse(answer,question)) // 4개 답변 모두 포함
                .build();
    }

    /**
     * Post 엔티티를 PostDetailResponseDTO로 변환
     */
    public static PostDetailDto toPostDetailResponse(Post post, List<PostAnswer> answers, List<String> questions, boolean isLiked) {

        for (String question : questions) {
            log.info("question = {}",question);
        }

        for (PostAnswer answer : post.getAnswers()) {
            log.info("answer= {}",answer.getId());
        }
        return PostDetailDto.builder()
                .postId(post.getId())
                .member(toDetailMemberInfo(post))
                .selectedDate(post.getCreatedAt().toLocalDate())
                .publicity(post.getPublicity())
                .answers(toDetailAnswerResponses(answers,questions))
                .selectiveDiary(toDetailSelectiveDiary(post))
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
                .username(post.getMember().getName())
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
    private static List<PostFeedResponseDTO.PostSummary.Answer> toDetailAnswerResponses(List<PostAnswer> answers, List<String> questions) {

        if (answers.isEmpty() || questions.isEmpty()) {
            log.warn("Empty answers or questions - Answers: {}, Questions: {}",
                    answers.size(), questions.size());
            return new ArrayList<>();
        }


        Map<Integer, PostAnswer> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        PostAnswer::getAnswerOrder,
                        answer -> answer,
                        (existing, replacement) -> existing  // 중복 시 기존 값 유지
                ));

        return IntStream.range(0, questions.size())
                .mapToObj(i -> {
                    int answerOrder = i + 1;  // 1,2,3,4
                    String questionContent = questions.get(i);
                    PostAnswer answer = answerMap.get(answerOrder);

                    if (answer != null) {
                        return toSafeAnswerResponse(answer, questionContent);
                    } else {
                        // 답변이 없는 경우 기본값 반환
                        log.warn("No answer found for order: {}", answerOrder);
                        return createEmptyAnswerResponse(questionContent);
                    }
                })
                .collect(Collectors.toList());

    }

    private static PostFeedResponseDTO.PostSummary.Answer createEmptyAnswerResponse(String questionContent) {
        return PostFeedResponseDTO.PostSummary.Answer.builder()
                .answerId(null)
                .answerType(null)
                .questionContent(questionContent!= null ? questionContent : "질문 없음")
                .imageAnswer(null)
                .textAnswer(null)
                .build();
    }

    private static PostFeedResponseDTO.PostSummary.Answer toSafeAnswerResponse(
            PostAnswer answer,
            String questionContent) {

        return PostFeedResponseDTO.PostSummary.Answer.builder()
                .answerId(answer.getId())
                .answerType(answer.getType())
                .questionContent(questionContent)
                .imageAnswer(answer.getType() == AnswerType.IMAGE ?
                        toSafeImageAnswer(answer.getPostAnswerImage()) : null)
                .textAnswer(answer.getType() == AnswerType.TEXT ?
                        toSafeTextAnswer(answer.getTextContent()) : null)
                .build();
    }


    /**
     * ✅ 안전한 이미지 답변 변환
     */
    private static PostFeedResponseDTO.PostSummary.Answer.ImageAnswer toSafeImageAnswer(PostAnswerImage image) {
        if (image == null) {
            return null;
        }

        return PostFeedResponseDTO.PostSummary.Answer.ImageAnswer.builder()
                .thumbnailUrl(image.getThumbnailUrl())
                .build();
    }

    /**
     * ✅ 안전한 텍스트 답변 변환
     */
    private static PostFeedResponseDTO.PostSummary.Answer.TextAnswer toSafeTextAnswer(String textContent) {
        if (textContent == null || textContent.trim().isEmpty()) {
            return null;
        }

        return PostFeedResponseDTO.PostSummary.Answer.TextAnswer.builder()
                .content(textContent)
                .build();
    }



    /**
     * PostAnswer를 Feed용 Answer DTO로 변환
     */
    private static PostFeedResponseDTO.PostSummary.Answer toFeedAnswerResponse(PostAnswer answer, String questionContent) {


        if(answer != null && questionContent != null) {

            return PostFeedResponseDTO.PostSummary.Answer.builder()
                    .answerId(answer.getId())
                    .answerType(answer.getType())
                    .questionContent(questionContent)
                    .imageAnswer(answer.getType() == AnswerType.IMAGE ?
                            toFeedImageAnswerResponse(answer.getPostAnswerImage()) : null)
                    .textAnswer(answer.getType() == AnswerType.TEXT ?
                            toFeedTextAnswerResponse(answer.getTextContent()) : null)
                    .build();

        } else {
            return createEmptyAnswerResponse(questionContent);
        }


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

    public static PostLikeDto toPostLikeDto(Long postId, boolean like)
    {
        return PostLikeDto.builder()
                .postId(postId)
                .liked(like)
                .build();
    }
}



