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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
public class PostController implements PostControllerDocs {

    private final PostCommandService postCommandService;

    @PostMapping
    public ApiResponse<PostResponseDTO.CreatePostResultDto> createPost(@CurrentMember Member member,
                                                                       @Valid @RequestBody PostRequestDTO.CreatePostDto request) {
        PostResponseDTO.CreatePostResultDto response = postCommandService.createEmptyPost(member,request);
        return ApiResponse.onSuccess(response);
    }




    /**
     * 텍스트 답변 작성/수정
     * Content-Type: application/json
     */

    @PutMapping("/{postId}/answers/{questionId}/text")
    public ApiResponse<PostResponseDTO.AnswerResultDto> updateTextAnswer(
            @CurrentMember Member member,
            @PathVariable Long postId,
            @PathVariable Integer questionId,
            @Valid @RequestBody PostRequestDTO.TextAnswerDto request) {

        log.info("텍스트 답변 수정 - 회원ID: {}, 게시글ID: {}, 질문타입: {}",
                member.getId(), postId, questionId);

        PostResponseDTO.AnswerResultDto response = postCommandService.updateTextAnswer(
                member,postId, questionId, request);


        return ApiResponse.onSuccess(response);
    }

    /**
     * 이미지 답변 작성/수정
     * Content-Type: multipart/form-data
     */
    @PutMapping("/{postId}/answers/{questionId}/image")
    public ApiResponse<PostResponseDTO.AnswerResultDto> updateImageAnswer(
            @CurrentMember Member member,
            @PathVariable Long postId,
            @PathVariable Integer questionId,
            @RequestPart("image") MultipartFile image) {

        log.info("이미지 답변 수정 - 회원ID: {}, 게시글ID: {}, 질문타입: {}, 파일명: {}",
                member.getId(), postId, questionId, image.getOriginalFilename());

        PostResponseDTO.AnswerResultDto response = postCommandService.updateImageAnswer(
                member, postId, questionId, image);

        return ApiResponse.onSuccess(response);
    }

    /**
     * 답변 삭제 (텍스트/이미지 공통)
     */
    @DeleteMapping("/{postId}/answers/{questionId}")
    public ApiResponse<String> deleteAnswer(
            @CurrentMember Member member,
            @PathVariable Long postId,
            @PathVariable Integer questionId) {

        log.info("답변 삭제 - 회원ID: {}, 게시글ID: {}, 질문타입: {}",
                member.getId(), postId, questionId);

//        postCommandService.deleteAnswer(member, postId, questionId);
        return ApiResponse.onSuccess("답변이 삭제되었습니다.");
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

    @PutMapping("/posts/{postId}/selective-diary")
    public ApiResponse<String> saveSelectiveDiary(Long postId, PostRequestDTO.SelectiveDiaryDto request) {
        return null;
    }

}
