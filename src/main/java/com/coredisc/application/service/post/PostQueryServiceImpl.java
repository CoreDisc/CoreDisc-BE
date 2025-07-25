package com.coredisc.application.service.post;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.PostConverter;
import com.coredisc.common.exception.handler.PostHandler;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import com.coredisc.domain.post.PostLikeRepository;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.todayQuestion.TodayQuestionRepository;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final TodayQuestionRepository todayQuestionRepository;

    @Override
    public List<Post> getTempPosts(Member member) {

        LocalDate today = LocalDate.now();

        List<Post> posts = postRepository.findTempPostByMemberAndDate(member,today);

        if(posts.isEmpty()) throw new PostHandler(ErrorStatus.POST_NOT_FOUND);

        return posts;
    }


    @Override
    public PostResponseDTO.TempPostDetailDto getTempPost(Member member, Long postId) {


        // 게시글 단건 조회
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // 임시저장된 게시글인지 확인
        if (post.getStatus() != PostStatus.TEMP) {
            throw new PostHandler(ErrorStatus.POST_ALREADY_PUBLISHED);
        }

        // 3. 작성자 본인인지 확인
        if (!post.getMember().getId().equals(member.getId())) {
            throw new PostHandler(ErrorStatus.NOT_POST_OWNER);
        }

        // 4. 해당 게시글의 답변들 조회 (1,2,3,4 순서로)
        List<PostAnswer> answers = postRepository.findTempPostWithAnswers(post.getId());


        return PostConverter.toTempPostDetailDto(post, answers);
    }

    @Override
    public PostResponseDTO.PostFeedResponseDTO findPostFeed(Member member, PostRequestDTO.PostFeedRequestDto request) {
        List<PostResponseDTO.PostFeedResponseDTO.PostSummary> posts = postRepository.findPostFeed(
                member,
                request.getFeedType(),
                request.getLastPostId(),
                request.getSize()
        );

        // hasNext 체크
        boolean hasNext = posts.size() > request.getSize();
        if (hasNext) {
            posts = posts.subList(0, request.getSize());
        }

        // nextCursor 설정
        Long nextCursor = null;
        if (hasNext && !posts.isEmpty()) {
            nextCursor = posts.get(posts.size() - 1).getPostId();
        }

        return PostConverter.toPostFeedResponseDto(posts, nextCursor,hasNext);
    }

    @Override
    public PostResponseDTO.PostDetailDto findPostDetail(Member member, Long postId) {
        Post post = postRepository.findPostDetail(member,postId);

        // 게시글 존재 여부 예외처리
        if(post == null) throw new PostHandler(ErrorStatus.POST_NOT_FOUND);

        // 발행 여부 예외처리
        if(post.isTemp()) throw new PostHandler(ErrorStatus.POST_NOT_READY_TO_PUBLISH);

        // 좋아요 여부 체크
        boolean isLiked = checkIsLiked(member.getId(),postId);

        LocalDate date = post.getCreatedAt().toLocalDate();

        List<TodayQuestion> questions = findQuestionContent(member,date);

        List<String> questionContents = questions.stream()
                .map(
                        TodayQuestion::getQuestionContent
                ).toList();

        return PostConverter.toPostDetailResponse(post,questionContents, isLiked);
    }

    private List<TodayQuestion> findQuestionContent(Member member, LocalDate date) {

        List<TodayQuestion> questions = new ArrayList<>();
        // 고정질문 3개 가져오기
        for(int i =1; i<4; i++) {
            LocalDate startOfMonth = date.withDayOfMonth(1);
            LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

            questions.add(todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(member,i,startOfMonth,endOfMonth).get());
        }

        questions.add(todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDate(member,4,date).get());
        // 랜덤질문 1개 가져오기

        return questions;

    }

    /**
     * 좋아요 여부 확인
     */
    private boolean checkIsLiked(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        return postLikeRepository.existsByMemberAndPost(member, post);
    }


}
