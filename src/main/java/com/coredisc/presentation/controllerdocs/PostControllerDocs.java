package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.common.enums.FeedType;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.post.PostRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
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
            @Parameter(description = "질문 타입 (1-4)", required = true) @PathVariable Integer questionOrder,
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
            @Parameter(description = "질문 타입 (1-4)", required = true) @PathVariable Integer questionOrder,
            @Parameter(description = "이미지 파일 (jpeg, jpg, png, gif, webp, 최대 10MB)",
                    content = @Content(mediaType = "multipart/form-data"))
            @RequestPart("image") MultipartFile image
            );


    @Operation(summary = "게시글 발행", description = "모든 답변과 선택형 일기 작성 후 게시글을 발행합니다.")
    ApiResponse<PostResponseDTO.PublishResultDto> publishPost(
            @CurrentMember Member member,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId,
            @Parameter(description = "게시글 발행 요청") @RequestBody PostRequestDTO.PublishPostDto request
    );

    @Operation(
            summary = "게시글 상세 조회",
            description = """
        게시글의 상세 정보를 조회합니다.
        - 4개의 답변과 선택형 일기 내용을 포함
        - 좋아요 여부도 함께 반환
        """
    )
    ApiResponse<PostResponseDTO.PostDetailDto> getPostDetail(
            @CurrentMember Member member,
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long postId
    );

    @Operation(
            summary = "게시글 피드 조회 (Pull 모델)",
            description = """
        Pull 모델로 게시글 피드를 실시간 조회합니다.
        - ALL: 팔로우하는 모든 사용자의 게시글
        - CORE: 친한친구로 설정한 사용자들의 게시글
        
        사용자가 요청할 때마다 실시간으로 팔로우 관계를 확인하여 피드를 생성합니다.
        cursor pagination을 사용하여 무한 스크롤을 지원합니다.
        """
    )
    ApiResponse<PostResponseDTO.PostFeedResponseDTO> getPosts(
            @CurrentMember Member member,
            @Parameter(description = "피드 타입 (ALL: 모든 팔로우, CORE: 친한친구)", example = "ALL") @RequestParam(defaultValue = "ALL") FeedType feedType,
            @Parameter(description = "커서 (마지막으로 조회한 게시글 ID)", example = "100")
            @RequestParam(required = false) Long cursor,
            @Parameter(description = "조회할 게시글 수", example = "10")
            @RequestParam(defaultValue = "10") Integer size
    );

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    ApiResponse<String> deletePost(
            @CurrentMember Member member , @Parameter(description = "게시글 ID", required = true)  @PathVariable Long postId
    );

    @Schema(name = "ImageUploadSchema", description = "이미지 파일만 전송하는 multipart 요청")
    public class ImageUploadSchema {
        @Schema(description = "이미지 파일", type = "string", format = "binary")
        public MultipartFile image;
    }

    @Operation(summary = "임시저장 게시글 ID로 조회", description = "임시저장된 게시글을 ID로 조회합니다.")
    @Parameters({
            @Parameter(name = "postId", description = "게시글 ID", example = "1")
    })
    ApiResponse<PostResponseDTO.TempPostDetailDto> getTempPost(
            @CurrentMember Member member,
            @PathVariable Long postId);

    @Operation(summary = "임시저장 게시글 조회", description = "특정 날짜의 임시저장된 게시글을 조회합니다.")
    ApiResponse<PostResponseDTO.TempAnswerPostDto> getTempPostByDate(
            @CurrentMember Member member
            );

}
