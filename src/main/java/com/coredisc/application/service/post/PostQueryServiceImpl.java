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
import com.coredisc.presentation.dto.post.PostRequestDTO;
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
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public PostResponseDTO.TempAnswerPostDto getTempPosts(Member member, LocalDate selectedDate) {

        List<Post> posts = postRepository.findTempPostByMemberAndDate(member,selectedDate);
        if(posts.isEmpty()) throw new PostHandler(ErrorStatus.POST_NOT_FOUND);

        // 가져온 포스트의 id를 반환 -> Converter 간단해서 바로 변환
        return PostResponseDTO.TempAnswerPostDto.builder()
                .PostIds(posts.stream()
                        .map(Post::getId)
                        .toList())
                .build();
    }


    @Override
    public PostResponseDTO.TempPostDetailDto getTempPost(Member member, Long postId) {


        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // 2. 임시저장 상태 확인
        if (post.getStatus() != PostStatus.TEMP) {
            throw new PostHandler(ErrorStatus.POST_ALREADY_PUBLISHED);
        }

        // 3. 작성자 본인 확인
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

        return PostConverter.toPostDetailResponse(post, isLiked);
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
