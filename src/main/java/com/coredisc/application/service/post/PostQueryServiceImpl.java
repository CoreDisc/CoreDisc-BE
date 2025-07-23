package com.coredisc.application.service.post;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.PostHandler;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;

    @Override
    public PostResponseDTO.TempAnswerPostDto getTempPost(Member member, LocalDate selectedDate) {

        List<Post> posts = postRepository.findTempPostByMemberAndDate(member,selectedDate);
        if(posts.isEmpty()) throw new PostHandler(ErrorStatus.POST_NOT_FOUND);

        // 가져온 포스트의 id를 반환
        return PostResponseDTO.TempAnswerPostDto.builder()
                .PostIds(posts.stream()
                        .map(Post::getId)
                        .toList())
                .build();
    }
}
