package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.member.MemberRequestDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Member", description = "멤버 관련 API")
public interface MemberControllerDocs {

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 기능입니다.")
    ApiResponse<String> resetPassword(@RequestBody @Valid MemberRequestDTO.ResetPasswordDTO request);

    @Operation(summary = "닉네임 변경", description = "닉네임 변경 기능입니다.")
    ApiResponse<String> resetNickname(@CurrentMember Member member, @RequestBody @Valid MemberRequestDTO.ResetNicknameDTO request);

    @Operation(summary = "계정 탈퇴", description = "계정 탈퇴 기능입니다.")
    ApiResponse<String> resignMember(@CurrentMember Member member);

    @Operation(summary = "마이홈 사용자 본인 정보 확인", description = "마이홈 사용자 본인 정보 확인 기능입니다.")
    ApiResponse<MemberResponseDTO.MyHomeInfoOfMeDTO> getMyHomeInfoOfMe(@CurrentMember Member member);

    @Operation(summary = "마이홈 타사용자 정보 확인", description = "마이홈 타사용자 정보 확인 기능입니다.")
    @Parameter(name = "targetUsername", description = "타사용자의 username(로그인 아이디)")
    ApiResponse<MemberResponseDTO.MyHomeInfoOfOtherDTO> getMyHomeInfoOfOther(@CurrentMember Member member, @PathVariable String targetUsername);
}
