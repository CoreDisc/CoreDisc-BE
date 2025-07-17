package com.coredisc.application.service.post;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface PostCommandService {

    public PostResponseDTO.CreatePostResultDto createEmptyPost(Member member, PostRequestDTO.CreatePostDto req);

    public PostResponseDTO.AnswerResultDto updateTextAnswer(Member member,Long postId, Integer questionId, PostRequestDTO.TextAnswerDto request);

    public PostResponseDTO.AnswerResultDto updateImageAnswer(Member member, Long postID, Integer questionId, MultipartFile image);

}
