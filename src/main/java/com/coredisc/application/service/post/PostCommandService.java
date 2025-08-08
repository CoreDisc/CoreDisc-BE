package com.coredisc.application.service.post;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

public interface PostCommandService {

    public PostResponseDTO.CreatePostResultDto createEmptyPost(Member member, PostRequestDTO.CreatePostDto req);

    public PostResponseDTO.AnswerResultDto updateTextAnswer(Member member,Long postId, Integer questionOrder, PostRequestDTO.TextAnswerDto request);

    public PostResponseDTO.AnswerResultDto updateImageAnswer(Member member, Long postID, Integer questionOrder, MultipartFile image);

    public Post publishPost(Member member, Long postId, PostRequestDTO.PublishPostDto request);

    void deletePost(Member member, Long postId);

    void cleanupOldTempPosts(LocalDate cutoffDate);
}
