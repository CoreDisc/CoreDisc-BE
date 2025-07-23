package com.coredisc.common.converter;

import com.coredisc.common.util.FileUtil;
import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import com.coredisc.presentation.dto.post.PostResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PostConverter {

    private PostConverter() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static PostResponseDTO.CreatePostResultDto toCreatePostResponse(Post post,
                                                                           List<TodayQuestion> todayQuestions,
                                                                           LocalDate selectedDate) {

        List<PostResponseDTO.TodayQuestionDto> questionDtos = todayQuestions.stream()
                .map(tq -> PostResponseDTO.TodayQuestionDto.builder()
                        .questionOrder(tq.getId())
                        .type(tq.getQuestionType())
                        .isAnswered(false)
                        .build())
                .collect(Collectors.toList());

        return PostResponseDTO.CreatePostResultDto.builder()
                .postId(post.getId())
                .memberId(post.getMember().getId())
                .selectedDate(selectedDate)
                .status(post.getStatus())
                .todayQuestions(questionDtos)
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResponseDTO.AnswerResultDto toAnswerResultDto(PostAnswer answer) {
        PostResponseDTO.ImageAnswerDto imageAnswer = null;
        PostResponseDTO.TextAnswerDto textAnswer = null;

        if (answer.getType() == AnswerType.IMAGE && answer.getPostAnswerImage() != null) {
            var image = answer.getPostAnswerImage();
            imageAnswer = PostResponseDTO.ImageAnswerDto.builder()
                    .imageUrl(image.getImgUrl())
                    .thumbnailUrl(image.getThumbnailUrl())
                    .originalFileName(image.getOriginalFileName())
                    .fileSize(image.getFileSize())
                    .fileSizeFormatted(FileUtil.formatFileSize(image.getFileSize() != null ? image.getFileSize() : 0))
                    .hasThumbnail(image.hasThumbnail())
                    .build();
        }

        if (answer.getType() == AnswerType.TEXT) {
            textAnswer = PostResponseDTO.TextAnswerDto.builder()
                    .content(answer.getTextContent())
                    .characterCount(answer.getTextContent() != null ? answer.getTextContent().length() : 0)
                    .build();
        }

        return PostResponseDTO.AnswerResultDto.builder()
                .answerId(answer.getId())
                .questionId(answer.getTodayQuestion().getId().intValue())
                .answerType(answer.getType())
                .imageAnswer(imageAnswer)
                .textAnswer(textAnswer)
                .createdAt(answer.getCreatedAt())
                .updatedAt(answer.getUpdatedAt())
                .build();
    }
    /**
     * 임시저장 게시글 상세 DTO 변환
     * @param post 임시저장 게시글
     * @param answers 답변 리스트
     * @return 임시저장 게시글 상세 DTO
     */
    public static PostResponseDTO.TempPostDetailDto toTempPostDetailDto(Post post, List<PostAnswer> answers) {

        // 답변을 questionOrder별로 매핑 (1,2,3,4)
        Map<Integer, PostAnswer> answerMap = answers.stream()
                .collect(Collectors.toMap(
                        answer -> answer.getTodayQuestion().getId().intValue(), // questionOrder
                        answer -> answer
                ));

        // 1,2,3,4 순서로 답변 DTO 생성
        List<PostResponseDTO.TempAnswerDto> answerDtos = IntStream.range(1,5)
                .mapToObj(questionOrder -> {
                    PostAnswer answer = answerMap.get(questionOrder);

                    if (answer == null) {
                        // 답변이 없는 경우
                        return PostResponseDTO.TempAnswerDto.builder()
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
                        return PostResponseDTO.TempAnswerDto.builder()
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

        return PostResponseDTO.TempPostDetailDto.builder()
                .postId(post.getId())
                .selectedDate(post.getCreatedAt().toLocalDate())
                .status(post.getStatus())
                .answers(answerDtos)
                .build();
    }

}



