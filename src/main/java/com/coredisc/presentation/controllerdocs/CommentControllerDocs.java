package com.coredisc.presentation.controllerdocs;


import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.comment.CommentRequestDTO;
import com.coredisc.presentation.dto.comment.CommentResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "댓글 API", description = "댓글 및 대댓글 관련 API")
public interface CommentControllerDocs {

    @Operation(summary = "댓글 작성", description = "특정 게시글에 댓글을 작성합니다.")
    ApiResponse<CommentResponseDTO.CommentCreateResponse> createComment(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable Long postId,
            @RequestBody CommentRequestDTO request,
            @CurrentMember Member member);

    @Operation(summary = "대댓글 작성", description = "특정 댓글에 대댓글을 작성합니다.")
    ApiResponse<CommentResponseDTO.CommentCreateResponse> createReply(
            @Parameter(description = "댓글 ID", required = true)
            @PathVariable Long commentId,
            @RequestBody CommentRequestDTO request,
            @CurrentMember Member member
    );

    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 댓글을 조회합니다.")
    ApiResponse<CursorDTO<CommentResponseDTO.CommentCreateResponse>> getParentComments(
            @Parameter(description = "게시글 ID", required = true)
            @PathVariable Long postId,
            @Parameter(description = "커서 ID")
            @RequestParam(required = false)
            Long cursorId,
            @Parameter
            @RequestParam
            Integer size,
            @CurrentMember
            Member member
    );

    @Operation(summary = "대댓글 목록 조회", description = "특정 댓글의 답글을 조회합니다.")
    ApiResponse<CursorDTO<CommentResponseDTO.CommentCreateResponse>> getChildComments(
            @Parameter(description = "댓글 ID", required = true)
            @PathVariable Long parentId,
            @Parameter(description = "커서 ID")
            @RequestParam(required = false)
            Long cursorId,
            @Parameter
            @RequestParam
            Integer size,
            @CurrentMember
            Member member
    );


    @Operation(summary = "댓글 삭제", description = "본인이 작성한 댓글을 삭제합니다.")
    ApiResponse<String> deleteComment(
            @Parameter(description = "댓글 ID", required = true)
            @PathVariable Long commentId,
            @CurrentMember Member member
    );
}
