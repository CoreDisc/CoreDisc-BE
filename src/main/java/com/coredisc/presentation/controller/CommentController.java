package com.coredisc.presentation.controller;

import com.coredisc.application.service.comment.CommentCommandService;
import com.coredisc.application.service.comment.CommentQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.CommentConverter;
import com.coredisc.domain.Comment;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.CommentControllerDocs;
import com.coredisc.presentation.dto.comment.CommentRequestDTO;
import com.coredisc.presentation.dto.comment.CommentResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController("/api/v1")
@RequiredArgsConstructor
public class CommentController implements CommentControllerDocs {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping("/posts/{postId}/comments")
    public ApiResponse<CommentResponseDTO.CommentCreateResponse> createComment(
            @PathVariable("postId")Long postId,
            @RequestBody CommentRequestDTO request,
            @CurrentMember Member member) {

        Comment comment = commentCommandService.createComment(postId,request,member.getId());

        return ApiResponse.onSuccess(CommentConverter.toCreateResponse(comment));
    }

    @PostMapping("/comments/{commentId}/replies")
    public ApiResponse<CommentResponseDTO.CommentCreateResponse> createReply(Long commentId, CommentRequestDTO request, Member member) {
        return null;
    }

    @GetMapping("/posts/{postId}/comments")
    public ApiResponse<CommentResponseDTO.CommentListResponse> getComments(Long postId, Pageable pageable) {
        return null;
    }

    @PutMapping("/comments/{commentId}")
    public ApiResponse<CommentResponseDTO.CommentUpdateResponse> updateComment(Long commentId, CommentRequestDTO request, Member member) {
        return null;
    }

    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<String> deleteComment(Long commentId, Member member) {
        return null;
    }
}
