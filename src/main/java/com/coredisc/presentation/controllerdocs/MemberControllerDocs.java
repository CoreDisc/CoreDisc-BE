package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberRequestDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import com.coredisc.presentation.dto.profileImg.ProfileImgResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Member", description = "멤버 관련 API")
public interface MemberControllerDocs {

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 기능입니다.")
    ApiResponse<String> resetPassword(@RequestBody @Valid MemberRequestDTO.ResetPasswordDTO request);

    @Operation(summary = "마이홈 닉네임, 아이디 변경", description = "닉네임, 아이디 변경 기능입니다.")
    ApiResponse<String> resetNicknameAndUsernameMyHome(@RequestHeader("accessToken") String accessToken,
                                                       @CurrentMember Member member,
                                                       @RequestBody @Valid MemberRequestDTO.MyHomeResetNicknameAndUsernameDTO request);

    @Operation(summary = "계정 탈퇴", description = "계정 탈퇴 기능입니다.")
    ApiResponse<String> resignMember(@CurrentMember Member member);

    @Operation(summary = "마이홈 본인 정보 조회", description = "마이홈 사용자 본인 정보 조회 기능입니다.")
    ApiResponse<MemberResponseDTO.MyHomeInfoDTO> getMyHomeInfo(@CurrentMember Member member);

    @Operation(summary = "마이홈 타사용자 정보 조회", description = "마이홈 타사용자 정보 조회 기능입니다.")
    @Parameter(name = "targetUsername", description = "타사용자의 username(로그인 아이디)")
    ApiResponse<MemberResponseDTO.UserHomeInfoDTO> getUserHomeInfo(@CurrentMember Member member, @PathVariable String targetUsername);

    @Operation(summary = "마이홈 본인 게시글 리스트 조회", description = "마이홈 본인 게시글 리스트 조회입니다. 커서 기반 페이징입니다.")
    @Parameters({
            @Parameter(name = "cursorId", description = "마지막으로 조회한 postId, 첫 요청 때는 null, queryString입니다."),
            @Parameter(name = "size", description = "기본값 10, queryString입니다.")
    })
    ApiResponse<CursorDTO<MemberResponseDTO.MyHomePostDTO>> getMyHomePosts(@CurrentMember Member member,
                                                                           @RequestParam(required = false) Long cursorId,
                                                                           @RequestParam(required = false) Integer size);

    @Operation(summary = "마이홈 타사용자 게시글 리스트 조회", description = "마이홈 타사용자 게시글 리스트 조회 기능입니다.")
    @Parameters({
            @Parameter(name = "targetUsername", description = "타사용자의 username(로그인 아이디), pathVariable입니다."),
            @Parameter(name = "cursorId", description = "마지막으로 조회한 postAnswerImgId, 첫 요청 때는 null, queryString입니다."),
            @Parameter(name = "size", description = "기본값 10, queryString입니다.")
    })
    ApiResponse<CursorDTO<MemberResponseDTO.MyHomePostDTO>> getUserHomePosts(@CurrentMember Member member,
                                                                               @PathVariable String targetUsername,
                                                                               @RequestParam(required = false) Long cursorId,
                                                                               @RequestParam(required = false) Integer size);

    @Operation(summary = "마이홈 계정 관리 이메일 변경", description = "계정 관리 이메일 변경 기능입니다.")
    ApiResponse<String> resetEmailMyHome(@CurrentMember Member member, @RequestBody MemberRequestDTO.MyHomeResetEmailDTO request);

    @Operation(summary = "마이홈 계정 관리 비밀번호 변경", description = "계정 관리 비밀번호 변경 기능입니다.")
    ApiResponse<String> resetPasswordMyHome(@CurrentMember Member member, @RequestBody MemberRequestDTO.MyHomeResetPasswordDTO request);

    @Operation(summary = "마이홈 계정 관리 아이디 변경", description = "계정 관리 아이디 변경 기능입니다.")
    ApiResponse<String> resetUsernameMyHome(@RequestHeader("accessToken") String accessToken,
                                            @CurrentMember Member member,
                                            @RequestBody MemberRequestDTO.MyHomeResetUsernameDTO request);

    @Schema(name = "ImageUploadSchema", description = "이미지 파일만 전송하는 multipart 요청")
    public class ImageUploadSchema {
        @Schema(description = "이미지 파일", type = "string", format = "binary")
        public MultipartFile image;
    }

    @Operation(summary = "프로필 사진 변경",
            description = "사용자 프로필 사진 변경 기능입니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = MemberControllerDocs.ImageUploadSchema.class))
            )
    )
    ApiResponse<ProfileImgResponseDTO.ProfileImgDTO> resetProfileImg(@CurrentMember Member member,
                                                                     @Parameter(description = "이미지 파일 (jpeg, jpg, png, gif, webp, 최대 10MB)",
                                                                             content = @Content(mediaType = "multipart/form-data"))
                                                                     @RequestPart("image") MultipartFile image);

    @Operation(summary = "기본 프로필 사진 변경", description = "사용자 기본 프로필 사진으로 변경 기능입니다.")
    ApiResponse<ProfileImgResponseDTO.ProfileImgDTO> resetToDefaultProfileImg(@CurrentMember Member member);

    @Operation(summary = "마이홈 내가 작성한 질문 리스트 조회", description = "마이홈 본인 질문 리스트 조회입니다. 커서 기반 페이징입니다.")
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리id입니다."),
            @Parameter(name = "cursorCreatedAt", description = "커서 - 마지막 질문 생성일자 (ISO 8601 형식), 첫 요청 때는 null"),
            @Parameter(name = "cursorQuestionType", description = "커서 - 마지막 질문 타입 (PERSONAL, OFFICIAL), 첫 요청 때는 null"),
            @Parameter(name = "cursorId", description = "마지막으로 조회한 질문의 id, 첫 요청 때는 null, queryString입니다."),
            @Parameter(name = "size", description = "기본값 10, queryString입니다.")
    })
    ApiResponse<CursorDTO<MemberResponseDTO.MyHomeQuestionDTO>> getMyHomeQuestions(@CurrentMember Member member,
                                                                               @RequestParam(name = "categoryId") Long categoryId,
                                                                               @RequestParam(name = "cursorCreatedAt", required = false) String cursorCreatedAt,
                                                                               @RequestParam(name = "cursorQuestionType", required = false) String cursorQuestionType,
                                                                               @RequestParam(name = "cursorId", required = false) Long cursorId,
                                                                               @RequestParam(name="size", required = false) Integer size);
}
