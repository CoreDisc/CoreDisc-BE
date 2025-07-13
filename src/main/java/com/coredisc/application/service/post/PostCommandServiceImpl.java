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
import com.coredisc.infrastructure.file.FileInfo;
import com.coredisc.infrastructure.file.FileStore;
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
    private final FileStore fileStore;

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

            answer = PostAnswer.builder()
                    .post(post)
                    .todayQuestion(todayQuestion)
                    .type(AnswerType.TEXT)
                    .textContent(request.getContent())
                    .questionContent(questionContent)
                    .build();

        }

        PostAnswer savedAnswer = postAnswerRepository.save(answer);

        return PostConverter.toAnswerResultDto(savedAnswer);
    }

    /**
     * 이미지 작성 및 수정 로직 구현
     */

    @Override
    @Transactional
    public PostResponseDTO.AnswerResultDto updateImageAnswer(Member member, Long postId, Integer questionType, MultipartFile image) {

        log.info("이미지 답변 수정 시작 - 회원ID: {}, 게시글ID: {}, 질문타입: {}, 파일: {}",
                member.getId(), postId, questionType, image.getOriginalFilename());

        // 1. 게시글 및 권한 확인
        Post post = validatePostOwnership(member, postId);

        // 2. 질문 조회 및 검증
        TodayQuestion todayQuestion = getTodayQuestion(member, questionType);

        // 3. 이미지 파일 저장
        FileInfo fileInfo = fileStore.storeFile(image, "post-answers");

        // 4. 기존 답변 조회
        Optional<PostAnswer> existingAnswer = postAnswerRepository
                .findByPostAndTodayQuestion(post, todayQuestion);

        PostAnswer answer;

        if (existingAnswer.isPresent()) {
            // 기존 답변 수정
            answer = existingAnswer.get();

            // 기존 이미지 파일 삭제
            deleteExistingImageIfPresent(answer);

            // 새 이미지로 교체
            PostAnswerImage newImage = createPostAnswerImage(answer, fileInfo);
            answer.updateToImageAnswer(newImage);

            log.info("기존 이미지 답변 수정 완료 - 답변ID: {}", answer.getId());

        } else {
            // 새로운 이미지 답변 생성
            String questionContent = extractQuestionContent(todayQuestion);

            answer = PostAnswer.builder()
                    .post(post)
                    .todayQuestion(todayQuestion)
                    .type(AnswerType.IMAGE)
                    .questionContent(questionContent)
                    .build();

            // 이미지 엔티티 생성 및 연결
            PostAnswerImage answerImage = createPostAnswerImage(answer, fileInfo);
            answer.updateToImageAnswer(answerImage);

            log.info("새 이미지 답변 생성 완료");
        }

        PostAnswer savedAnswer = postAnswerRepository.save(answer);

        log.info("이미지 답변 처리 완료 - 답변ID: {}, 이미지URL: {}",
                savedAnswer.getId(), fileInfo.getFileUrl());

        return PostConverter.toAnswerResultDto(savedAnswer);
    }


    private PostAnswerImage createPostAnswerImage(PostAnswer answer, FileInfo fileInfo) {
        return PostAnswerImage.builder()
                .postAnswer(answer)
                .imgUrl(fileInfo.getFileUrl())
                .thumbnailUrl(fileInfo.getThumbnailUrl())
                .originalFileName(fileInfo.getOriginalFileName())
                .storedFileName(fileInfo.getStoredFileName())
                .filePath(fileInfo.getFilePath())
                .fileSize(fileInfo.getFileSize())
                .build();
    }

    /**
     * 기존 이미지 파일 삭제
     */
    private void deleteExistingImageIfPresent(PostAnswer answer) {
        if (answer.getType() == AnswerType.IMAGE && answer.getPostAnswerImage() != null) {
            PostAnswerImage existingImage = answer.getPostAnswerImage();

            // 실제 파일 삭제
            if (existingImage.getFilePath() != null) {
                fileStore.deleteFile(existingImage.getFilePath());
            }

            // DB에서 이미지 엔티티 삭제
            postAnswerImageRepository.delete(existingImage);

            log.info("기존 이미지 파일 삭제 완료 - 경로: {}", existingImage.getFilePath());
        }
    }

    /**
     * 질문 내용 추출
     */
    private String extractQuestionContent(TodayQuestion todayQuestion) {
        if (todayQuestion.getOfficialQuestion() != null) {
            return todayQuestion.getOfficialQuestion().getContents();
        } else if (todayQuestion.getPersonalQuestion() != null) {
            return todayQuestion.getPersonalQuestion().getContent();
        } else {
            return "질문 내용을 찾을 수 없습니다.";
        }
    }



    private void validatePostNotExists(Member member, LocalDate selectedDate) {
        // TODO : 게시글이 이미 있다면 예외를 던짐. - 오늘 날짜 기준 published 인 게시글이 있다면?
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
