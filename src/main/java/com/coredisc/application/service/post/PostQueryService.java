package com.coredisc.application.service.post;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface PostQueryService {

    /**
     * 임시저장된 게시글 불러오기 (TEMP 상태)
     * 오늘의 질문에 대한 답변만 해당
     *
     * @param member 현재 사용자
     * @return 임시저장된 게시글 정보
     */
    List<Post> getTempPosts(Member member);

    PostResponseDTO.TempPostDetailDto getTempPost(Member member, Long postId);

    PostResponseDTO.PostFeedResponseDTO findPostFeed(Member member, PostRequestDTO.PostFeedRequestDto request);

    PostResponseDTO.PostDetailDto findPostDetail(Member member, Long postId);
}
