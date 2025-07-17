package com.coredisc.common.converter;

import com.coredisc.common.util.FileUtil;
import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import com.coredisc.presentation.dto.post.PostResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
}



