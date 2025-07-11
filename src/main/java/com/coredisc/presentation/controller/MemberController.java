package com.coredisc.presentation.controller;

import com.coredisc.application.service.member.MemberCommandService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.controllerdocs.MemberControllerDocs;
import com.coredisc.presentation.dto.member.MemberRequestDTO;
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
}
