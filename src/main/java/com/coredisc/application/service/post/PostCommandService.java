package com.coredisc.application.service.post;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;

public interface PostCommandService {

    public PostResponseDTO.CreatePostResultDto createEmptyPost(Member member, PostRequestDTO.CreatePostDto req);

}
