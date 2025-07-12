package com.coredisc.presentation.controller;

import com.coredisc.application.service.post.PostCommandService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.PostControllerDocs;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import com.coredisc.security.auth.PrincipalDetails;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController implements PostControllerDocs {

    private final PostCommandService postCommandService;

    @PostMapping
    public ApiResponse<PostResponseDTO.CreatePostResultDto> createPost(@CurrentMember Member member,
                                                                       @Valid @RequestBody PostRequestDTO.CreatePostDto request) {
        PostResponseDTO.CreatePostResultDto response = postCommandService.createEmptyPost(member,request);
        return ApiResponse.onSuccess(response);
    }



    @PutMapping("/{postId}/publish")
    public ApiResponse<PostResponseDTO.PublishResultDto> publishPost(Long postId, PostRequestDTO.PublishPostDto request) {
        return null;
    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDTO.PostDetailDto> getPost(Long postId) {
        return null;
    }

    @GetMapping
    public ApiResponse<PostResponseDTO.PostListDto> getPosts(String type, Pageable pageable) {
        return null;
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(Long postId) {
        return null;
    }

    @PutMapping("/{postId}/answers/{answerType}")
    public ApiResponse<PostResponseDTO.AnswerResultDto> createOrUpdateAnswer(Long postId, Integer answerType, PostRequestDTO.TextAnswerDto request) {
        return null;
    }

    @DeleteMapping("/{postId}/answers/{answerType}")
    public ApiResponse<String> deleteAnswer(Long postId, Integer answerType) {
        return null;
    }

    @PutMapping("/posts/{postId}/selective-diary")
    public ApiResponse<String> saveSelectiveDiary(Long postId, PostRequestDTO.SelectiveDiaryDto request) {
        return null;
    }

}
