package com.coredisc.application.service.post;


import com.coredisc.common.converter.PostConverter;
import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.common.enums.QuestionType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostCommandServiceImpl implements PostCommandService {
    private final PostRepository postRepository;

    //  빈 게시글 생성
    @Override
    public PostResponseDTO.CreatePostResultDto createEmptyPost(Member member, PostRequestDTO.CreatePostDto req) {

        LocalDate selectedDate = req.getSelectedDate();

        log.info("빈 게시글 생성 시작 - 회원ID: {}, 날짜: {}", member.getId(), selectedDate);

        // 중복 검증 - 해당 날짜에 이미 포스트가 등록되었는지.
        validatePostNotExists(member);

        // 오늘의 질문 확인 - TODO : todayquestion 더미데이터 대신 실제 DB 에서 가져올 것
        List<TodayQuestion> todayQuestions = validateTodayQuestions(member);

        Post emptyPost = Post.builder()
                .member(member)
                .status(PostStatus.TEMP)
                .likeCount(0)
                .commentCount(0)
                .viewCount(0)
                .build();

        Post savedPost = postRepository.save(emptyPost);

        return PostConverter.toCreatePostResponse(savedPost, todayQuestions, selectedDate);

    }

    private void validatePostNotExists(Member member) {
        // 게시글이 이미 있다면 예외를 던짐. - 오늘 날짜 기준 published 인 게시글이 있다면?
    }

    //TODO : 나중에 respository 와 연결하기
    private List<TodayQuestion> validateTodayQuestions(Member member) {
        /*List<TodayQuestion> todayQuestions = todqyQuestionRepository
                .findByMember(
                        member
                );*/

        List<TodayQuestion> todayQuestions = new ArrayList<>();

        // 임의로 4개 더미데이터를 생성
        for (int i = 0; i < 3; i++) {
            todayQuestions.add(
                    TodayQuestion.builder().
                            questionType(QuestionType.RANDOM)
                            .member(member)
                            .selectedDate(LocalDateTime.now()).
                            build()
            );
        }

        todayQuestions.add(TodayQuestion.builder().
                questionType(QuestionType.FIXED)
                .member(member)
                .selectedDate(LocalDateTime.now()).
                build()
        );

//        if (todayQuestions.size() != 4) {
//            throw new PostHandler(ErrorStatus.INCOMPLETE_TODAY_QUESTIONS);
//
//        }
        return todayQuestions;
    }
}
