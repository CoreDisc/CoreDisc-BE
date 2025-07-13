package com.coredisc.presentation.controller;

import com.coredisc.application.service.member.MemberCommandService;
import com.coredisc.application.service.member.MemberQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.MemberControllerDocs;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberRequestDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController implements MemberControllerDocs {

    private static final int DEFAULT_SIZE = 10;

    private final MemberCommandService memberCommandService;
    private final MemberQueryService memberQueryService;

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
    public ApiResponse<String> resignMember(@CurrentMember Member member) {

        memberCommandService.resignMember(member);
        return ApiResponse.onSuccess("계정이 탈퇴되었습니다.");
    }

    @Override
    @GetMapping("/my-home")
    public ApiResponse<MemberResponseDTO.MyHomeInfoOfMeDTO> getMyHomeInfoOfMe(@CurrentMember Member member) {

        return ApiResponse.onSuccess(memberQueryService.getMyHomeInfoOfMe(member));
    }

    @Override
    @GetMapping("/my-home/{targetUsername}")
    public ApiResponse<MemberResponseDTO.MyHomeInfoOfOtherDTO> getMyHomeInfoOfOther(@CurrentMember Member member,
                                                                                 @PathVariable String targetUsername) {
        return ApiResponse.onSuccess(memberQueryService.getMyHomeInfoOfOther(member, targetUsername));
    }

    @Override
    @GetMapping("/my-home/post-images")
    public ApiResponse<CursorDTO<MemberResponseDTO.MyHomeImageAnswerDTO>> getMyHomeImageAnswers(@CurrentMember Member member,
                                                                                                @RequestParam(required = false) Long cursorId,
                                                                                                @RequestParam(required = false) Integer size) {
        if(size == null) { size = DEFAULT_SIZE; }

        return ApiResponse.onSuccess(memberQueryService.getMyHomeImageAnswers(member, cursorId, PageRequest.of(0, size)));
    }
}
