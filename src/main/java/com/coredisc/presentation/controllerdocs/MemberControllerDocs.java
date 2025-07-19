package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberRequestDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

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
    ApiResponse<CursorDTO<MemberResponseDTO.UserHomePostDTO>> getUserHomePosts(@CurrentMember Member member,
                                                                               @PathVariable String targetUsername,
                                                                               @RequestParam(required = false) Long cursorId,
                                                                               @RequestParam(required = false) Integer size);

    @Operation(summary = "마이홈 계정 관리 이메일 변경", description = "계정 관리 이메일 변경 기능입니다.")
    ApiResponse<String> resetEmailMyHome(@CurrentMember Member member, @RequestBody MemberRequestDTO.MyHomeResetEmailDTO request);

    @Operation(summary = "마이홈 계정 관리 비밀번호 변경", description = "계정 관리 비밀번호 변경 기능입니다.")
    ApiResponse<String> resetPasswordMyHome(@CurrentMember Member member, @RequestBody MemberRequestDTO.MyHomeResetPasswordDTO request);
}
