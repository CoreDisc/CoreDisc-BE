package com.coredisc.common.converter;

import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.post.Post;
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
}
