package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "게시글",description = "게시글 관련 api")
public interface PostControllerDocs {

    @Operation(summary = "게시글 생성 (임시저장)", description = "오늘의 질문에 대한 게시글을 생성합니다. (임시저장 상태)")
    ApiResponse<PostResponseDTO.CreatePostResultDto> createPost(
            @Parameter(description = "게시글 생성 요청") @RequestBody PostRequestDTO.CreatePostDto request
    );

    @Operation(summary = "게시글 발행", description = "모든 답변과 선택형 일기 작성 후 게시글을 발행합니다.")
    ApiResponse<PostResponseDTO.PublishResultDto> publishPost(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "게시글 발행 요청") @RequestBody PostRequestDTO.PublishPostDto request
    );

    @Operation(summary = "게시글 상세 조회", description = "게시글의 모든 정보를 조회합니다.")
    ApiResponse<PostResponseDTO.PostDetailDto> getPost(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId
    );

    @Operation(summary = "게시글 목록 조회 (피드)", description = "게시글 목록을 조회합니다.")
    ApiResponse<PostResponseDTO.PostListDto> getPosts(
            @Parameter(description = "피드 타입 (FOLLOWING, PUBLIC)") @RequestParam(defaultValue = "PUBLIC") String type,
            @Parameter(description = "페이징 정보") Pageable pageable
    );

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    ApiResponse<String> deletePost(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId
    );

    @Operation(summary = "답변 작성/수정", description = "질문에 대한 답변을 작성하거나 수정합니다.")
    ApiResponse<PostResponseDTO.AnswerResultDto> createOrUpdateAnswer(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "질문 타입 (0-3)", required = true) @PathVariable Integer answerType,
            @Parameter(description = "답변 요청") @RequestBody PostRequestDTO.AnswerDto request
    );

    @Operation(summary = "답변 삭제", description = "특정 답변을 삭제합니다.")
    ApiResponse<String> deleteAnswer(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "질문 타입 (0-3)", required = true) @PathVariable Integer answerType
    );

    @Operation(summary = "선택형 일기 임시 저장", description = "게시글 발행 전 선택형 일기를 임시 저장합니다.")
    ApiResponse<String> saveSelectiveDiary(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "선택형 일기 요청") @RequestBody PostRequestDTO.SelectiveDiaryDto request
    );


}
