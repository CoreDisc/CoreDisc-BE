package com.coredisc.application.service.post;


import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.PostConverter;
import com.coredisc.common.exception.handler.PostHandler;
import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.common.enums.QuestionType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.*;
import com.coredisc.infrastructure.repository.answer.TodayQuestionRepository;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostCommandServiceImpl implements PostCommandService {
    private final PostRepository postRepository;
    private final PostAnswerRepository postAnswerRepository;
    private final PostAnswerImageRepository postAnswerImageRepository;
    private final TodayQuestionRepository todayQuestionRepository;
    //  빈 게시글 생성
    @Override
    @Transactional
    public PostResponseDTO.CreatePostResultDto createEmptyPost(Member member, PostRequestDTO.CreatePostDto req) {

        LocalDate selectedDate = req.getSelectedDate();

        log.info("빈 게시글 생성 시작 - 회원ID: {}, 날짜: {}", member.getId(), selectedDate);

        // 중복 검증 - 해당 날짜에 이미 포스트가 등록되었는지.
        validatePostNotExists(member,selectedDate);

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


    //TODO : requestBody 로 Type 을 받아야 하는가?
    @Override
    public PostResponseDTO.AnswerResultDto updateTextAnswer(Member member,Long postId, Integer questionId, PostRequestDTO.TextAnswerDto request) {

        //게시글 및 권한 확인
        Post post = validatePostOwnership(member,postId);

        log.info("게시판 가져오기 게시글ID: {}",post.getId() );

        // 질문 조회 + 검증
        TodayQuestion todayQuestion = getTodayQuestion(member,questionId);

        log.info("질문ID: {}",todayQuestion.getId());

        // 기존 답변 or null
        Optional<PostAnswer> existingAnswer = postAnswerRepository
                .findByPostAndTodayQuestion(post,todayQuestion);

        PostAnswer answer;

        if(existingAnswer.isPresent()) {

            log.info("답변이 이미 존재해 수정을 진행합니다.");
            // 수정
            answer = existingAnswer.get();


            // 기존이 이미지인 경우 - 삭제
            if(answer.isImageAnswer() && answer.getPostAnswerImage() != null) {
                postAnswerImageRepository.delete(answer.getPostAnswerImage());
            }

            answer.updateTextAnswer(request.getContent());

        } else {
            log.info("답변을 생성합니다.");
            // 질문 저장 - > 만약 질문이 발행 질문이라면? 발행 질문 content 를 가져온다.
            String questionContent;

            if(todayQuestion.getOfficialQuestion() != null) {
                questionContent = todayQuestion.getOfficialQuestion().getContents();
            } else if(todayQuestion.getPersonalQuestion() != null) {
                questionContent = todayQuestion.getPersonalQuestion().getContent();
            } else {
                //TODO: 어떤 질문도 참고하지 않을 때 - 에러
                questionContent= "nothing";
            }

            log.info("questioinContent:{}",questionContent);

            // 임시 저장용 temp -> 실제 TodayQuestion 으로 대체
            TodayQuestion tempTodayQuestion = TodayQuestion.builder().questionType(QuestionType.RANDOM)
                    .member(member)
                    .selectedDate(LocalDateTime.now())
                    .build();

            todayQuestionRepository.save(tempTodayQuestion); // 영속화

            answer = PostAnswer.builder()
                    .post(post)
                    .todayQuestion(tempTodayQuestion)
                    .type(AnswerType.TEXT)
                    .textContent(request.getContent())
                    .questionContent(questionContent)
                    .build();

        }

        PostAnswer savedAnswer = postAnswerRepository.save(answer);



        return PostConverter.toAnswerResultDto(savedAnswer);
    }

    @Override
    public PostResponseDTO.AnswerResultDto updateImageAnswer(Member member, Long postID, Integer questionId, MultipartFile image) {
        return null;

    }


    private void validatePostNotExists(Member member, LocalDate selectedDate) {
        // 게시글이 이미 있다면 예외를 던짐. - 오늘 날짜 기준 published 인 게시글이 있다면?
    }

    //TODO : 나중에 respository 와 연결하기
    private List<TodayQuestion> validateTodayQuestions(Member member) {
        List<TodayQuestion> todayQuestions = todayQuestionRepository.findByMember(member);

        if (todayQuestions.size() != 4) {
            throw new PostHandler(ErrorStatus.INCOMPLETE_TODAY_QUESTIONS);

        }

        return todayQuestions;
    }

    private Post validatePostOwnership(Member member, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow( () -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // 실제 포스트 작성자인지
        if(!post.getMember().getId().equals(member.getId())) {
            throw new PostHandler(ErrorStatus.NOT_POST_OWNER);
        }

        if(post.isPublished()) {
            throw new PostHandler(ErrorStatus.POST_ALREADY_PUBLISHED);
        }

        return post;

    }

    private TodayQuestion getTodayQuestion(Member member, Integer questionId) {
        // 실제로는 Post와 연결된 TodayQuestion들 중에서 questionOrder에 해당하는 것을 찾아야 함
        // 현재는 간단히 구현
        List<TodayQuestion> todayQuestions = todayQuestionRepository.findByMember(member);

        if (questionId < 0 || questionId >= todayQuestions.size()) {
            throw new PostHandler(ErrorStatus.INVALID_QUESTION_ORDER);
        }

        return todayQuestions.get(questionId);
    }

}
