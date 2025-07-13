package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "게시글",description = "게시글 관련 api")
public interface PostControllerDocs {

    @Operation(summary = "게시글 생성 (임시저장)", description = "오늘의 질문에 대한 게시글을 생성합니다. (임시저장 상태)")
    ApiResponse<PostResponseDTO.CreatePostResultDto> createPost(
            @CurrentMember Member member,
            @Parameter(description = "게시글 생성 요청") @RequestBody PostRequestDTO.CreatePostDto request
    );

    @Operation(summary = "글 답변 작성/수정", description = "질문에 대한 글 답변을 작성하거나 수정합니다.")
    ApiResponse<PostResponseDTO.AnswerResultDto> updateTextAnswer(
            @CurrentMember Member member,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "질문 타입 (0-3)", required = true) @PathVariable Integer questionId,
            @Parameter(description = "답변 요청") @RequestBody PostRequestDTO.TextAnswerDto request
    );


    @Operation(
            summary = "이미지 답변 등록 또는 수정",
            description = "질문에 대한 이미지 답변을 작성하거나 수정합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = ImageUploadSchema.class))
            )
    )
    ApiResponse<PostResponseDTO.AnswerResultDto> updateImageAnswer(
            @CurrentMember Member member,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "질문 타입 (0-3)", required = true) @PathVariable Integer questionId,
            @Parameter(description = "이미지 파일 (jpeg, jpg, png, gif, webp, 최대 10MB)",
                    content = @Content(mediaType = "multipart/form-data"))
            @RequestPart("image") MultipartFile image
            );

    @Operation(summary = "답변 삭제", description = "특정 답변을 삭제합니다.")
    ApiResponse<String> deleteAnswer(
            @CurrentMember Member member,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "질문 타입 (0-3)", required = true) @PathVariable Integer questionId
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

    @Operation(summary = "선택형 일기 임시 저장", description = "게시글 발행 전 선택형 일기를 임시 저장합니다.")
    ApiResponse<String> saveSelectiveDiary(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "선택형 일기 요청") @RequestBody PostRequestDTO.SelectiveDiaryDto request
    );

    @Schema(name = "ImageUploadSchema", description = "이미지 파일만 전송하는 multipart 요청")
    public class ImageUploadSchema {
        @Schema(description = "이미지 파일", type = "string", format = "binary")
        public MultipartFile image;
    }

}
