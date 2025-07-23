package com.coredisc.presentation.controller;

import com.coredisc.application.service.post.PostCommandService;
import com.coredisc.application.service.post.PostQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.common.enums.FeedType;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.PostControllerDocs;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/posts")
public class PostController implements PostControllerDocs {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;

    @PostMapping
    public ApiResponse<PostResponseDTO.CreatePostResultDto> createPost(@CurrentMember Member member,
                                                                       @Valid @RequestBody PostRequestDTO.CreatePostDto request) {
        PostResponseDTO.CreatePostResultDto response = postCommandService.createEmptyPost(member, request);
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
                member, postId, questionId, request);


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


    /**
     * 임시 저장된 게시글 조회
     */

    @GetMapping("/posts/temp/{postId}")
    public ApiResponse<PostResponseDTO.TempPostDetailDto> getTempPost(Member member, Long postId) {
        PostResponseDTO.TempPostDetailDto response = postQueryService.getTempPost(member,postId);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/posts/temp")
    public ApiResponse<PostResponseDTO.TempAnswerPostDto> getTempPostByDate(Member member, LocalDate selectedDate) {
        PostResponseDTO.TempAnswerPostDto response = postQueryService.getTempPosts(member,selectedDate);
        //Converter 클래스가 변환해야함.
        return ApiResponse.onSuccess(response);
    }

    @PutMapping("/{postId}/publish")
    public ApiResponse<PostResponseDTO.PublishResultDto> publishPost(
            @CurrentMember Member member,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequestDTO.PublishPostDto request) {

        return ApiResponse.onSuccess(postCommandService.publishPost(member, postId, request));

    }

    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDTO.PostDetailDto> getPostDetail(@CurrentMember Member member, @PathVariable("postId") Long postId) {
        return ApiResponse.onSuccess(postQueryService.findPostDetail(member,postId));
    }


    @GetMapping
    public ApiResponse<PostResponseDTO.PostFeedResponseDTO> getPosts(
            @CurrentMember Member member,
            FeedType feedType, Long cursor, Integer size) {
        if(size >30 ) {
            size =30;
        }

        PostRequestDTO.PostFeedRequestDto request = PostRequestDTO.PostFeedRequestDto.
                builder()
                .feedType(feedType)
                .lastPostId(cursor)
                .size(size)
                .build();

        PostResponseDTO.PostFeedResponseDTO response = postQueryService.findPostFeed(member,request);
        return ApiResponse.onSuccess(response);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(
            @CurrentMember Member member,
            @PathVariable Long postId
    ) {
        log.info("게시글 삭제 요청 - 회원ID: {}, 게시글ID: {}", member.getId(), postId);

        postCommandService.deletePost(member,postId);

        return ApiResponse.onSuccess("게시글이 삭제되었습니다.");
    }


}
