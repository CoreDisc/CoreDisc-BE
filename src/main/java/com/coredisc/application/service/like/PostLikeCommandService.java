package com.coredisc.application.service.like;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.PostLike;
import com.coredisc.presentation.dto.post.PostResponseDTO;


public interface PostLikeCommandService {
    // 좋아요 테이블추가
    PostResponseDTO.PostLikeDto create(Long postId, Member member);
}
