package com.coredisc.presentation.controller;

import com.coredisc.application.service.member.MemberCommandService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.MemberControllerDocs;
import com.coredisc.presentation.dto.member.MemberRequestDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

    private final MemberCommandService memberCommandService;

    @Override
    @PatchMapping("/password")
    public ApiResponse<String> resetPassword(@RequestBody @Valid MemberRequestDTO.ResetPasswordDTO request) {

        memberCommandService.resetPassword(request);
        return ApiResponse.onSuccess("비밀번호가 성공적으로 변경되었습니다.");
    }

    @Override
    @PatchMapping("/nickname")
    public ApiResponse<String> resetNickname(@CurrentMember Member member,
                                             @RequestBody @Valid MemberRequestDTO.ResetNicknameDTO request) {

        memberCommandService.resetNickname(member, request);
        return ApiResponse.onSuccess("닉네임이 성공적으로 변경되었습니다.");
    }

    @Override
    @PatchMapping("/resign")
    public ApiResponse<String> resignMember(Member member) {

        memberCommandService.resignMember(member);
        return ApiResponse.onSuccess("계정이 탈퇴되었습니다.");
    }
}
