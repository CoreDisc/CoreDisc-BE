package com.coredisc.presentation.controller;

import com.coredisc.application.service.comment.CommentCommandService;
import com.coredisc.application.service.comment.CommentQueryService;
import com.coredisc.application.service.comment.CommentQueryServiceImpl;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.CommentConverter;
import com.coredisc.domain.Comment;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.CommentControllerDocs;
import com.coredisc.presentation.dto.comment.CommentRequestDTO;
import com.coredisc.presentation.dto.comment.CommentResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/api/")
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
    public ApiResponse<CommentResponseDTO.CommentCreateResponse> createReply(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDTO request,
            @CurrentMember Member member) {

        Comment comment = commentCommandService.createReply(commentId,request,member.getId());

        return ApiResponse.onSuccess(CommentConverter.toReplyCreateResponse(comment));
    }

    @GetMapping("/posts/{postId}/comments")
    public ApiResponse<CursorDTO<CommentResponseDTO.CommentCreateResponse>> getParentComments(
            @PathVariable Long postId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "20") Integer size,
            @CurrentMember Member member)
    {
        return ApiResponse.onSuccess(commentQueryService.getParentComments(postId,cursorId,size,member));
    }

    @GetMapping("/comments/{commentId}/replies")
    public ApiResponse<CursorDTO<CommentResponseDTO.CommentCreateResponse>> getChildComments(
            @PathVariable("commentId") Long parentId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "20") Integer size,
            @CurrentMember Member member) {

        return ApiResponse.onSuccess(commentQueryService.getChildComments(parentId,cursorId,size,member));
    }

    @DeleteMapping("/comments/{commentId}")
    public ApiResponse<String> deleteComment(
            @PathVariable Long commentId,
            @CurrentMember Member member) {

        commentCommandService.deleteComment(commentId, member.getId());

        return ApiResponse.onSuccess("댓글을 삭제했습니다.");
    }


}
