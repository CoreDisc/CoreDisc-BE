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
    @PatchMapping("/profile")
    public ApiResponse<String> resetNicknameAndUsername(@RequestHeader("accessToken") String accessToken,
                                                        @CurrentMember Member member,
                                                        @RequestBody @Valid MemberRequestDTO.ResetNicknameAndUsernameDTO request) {

        boolean isUsernameChanged = memberCommandService.resetNicknameAndUsername(accessToken, member, request);

        // username이 변경되었을 시, 토큰 재발급 요청
        if(isUsernameChanged) {
            return ApiResponse.onSuccess("아이디가 변경되어 인증이 만료되었습니다. 다시 로그인 해주세요.");
        }

        // nickname만 변경되었을 시
        return ApiResponse.onSuccess("성공적으로 변경되었습니다.");
    }

    @Override
    @PatchMapping("/resign")
    public ApiResponse<String> resignMember(@CurrentMember Member member) {

        memberCommandService.resignMember(member);
        return ApiResponse.onSuccess("계정이 탈퇴되었습니다.");
    }

    @Override
    @GetMapping("/my-home")
    public ApiResponse<MemberResponseDTO.MyHomeInfoDTO> getMyHomeInfo(@CurrentMember Member member) {

        return ApiResponse.onSuccess(memberQueryService.getMyHomeInfo(member));
    }

    @Override
    @GetMapping("/my-home/{targetUsername}")
    public ApiResponse<MemberResponseDTO.UserHomeInfoDTO> getUserHomeInfo(@CurrentMember Member member,
                                                                          @PathVariable String targetUsername) {
        return ApiResponse.onSuccess(memberQueryService.getUserHomeInfo(member, targetUsername));
    }

    @Override
    @GetMapping("/my-home/posts")
    public ApiResponse<CursorDTO<MemberResponseDTO.MyHomePostDTO>> getMyHomePosts(@CurrentMember Member member,
                                                                                  @RequestParam(required = false) Long cursorId,
                                                                                  @RequestParam(required = false) Integer size) {
        if(size == null) { size = DEFAULT_SIZE; }

        return ApiResponse.onSuccess(memberQueryService.getMyHomePosts(member, cursorId, PageRequest.of(0, size)));
    }

    @Override
    @GetMapping("/my-home/post-images/{targetUsername}")
    public ApiResponse<CursorDTO<MemberResponseDTO.UserHomeImageAnswerDTO>> getUserHomeImageAnswers(@CurrentMember Member member,
                                                                                                    @PathVariable String targetUsername,
                                                                                                    @RequestParam(required = false) Long cursorId,
                                                                                                    @RequestParam(required = false) Integer size) {
        if(size == null) { size = DEFAULT_SIZE; }

        return ApiResponse.onSuccess(memberQueryService.getUserHomeImageAnswers(member, targetUsername, cursorId, PageRequest.of(0, size)));
    }
}
