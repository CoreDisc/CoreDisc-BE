package com.coredisc.presentation.controller;

import com.coredisc.application.service.auth.AuthCommandService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.presentation.controllerdocs.AuthControllerDocs;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthCommandService authCommandService;

    // 회원가입
    @PostMapping("/signup")
    public ApiResponse<AuthResponseDTO.SignupResultDTO> signup(@Valid @RequestBody AuthRequestDTO.SignupDTO request) {

        return ApiResponse.onSuccess(MemberConverter.toSignupResultDTO(
                authCommandService.signup(request)
        ));
    }

    // 로그인 아이디 중복 검사
    @GetMapping("/check-username")
    public ApiResponse<AuthResponseDTO.CheckUsernameResultDTO> checkUsername(@RequestParam String username) {
        return null;
    }

    // 이메일 중복 검사
    @GetMapping("/check-email")
    public ApiResponse<AuthResponseDTO.CheckEmailResultDTO> checkEmail(@RequestParam String email) {
        return null;
    }

    // 닉네임 중복 검사
    @GetMapping("/check-nickname")
    public ApiResponse<AuthResponseDTO.CheckNicknameResultDTO> checkNickname(@RequestParam String nickname) {
        return null;
    }

    // 인증코드 이메일 발송
    @PostMapping("/send-code")
    public ApiResponse<String> sendCode(@RequestBody @Valid AuthRequestDTO.VerifyEmailDTO request) {
        authCommandService.sendCode(request);
        return ApiResponse.onSuccess("인증 메일이 성공적으로 전송되었습니다.");
    }

    // 인증코드 검증
    @PostMapping("/verify-code")
    public ApiResponse<AuthResponseDTO.VerifyCodeResultDTO> verifyCode(@RequestBody @Valid AuthRequestDTO.VerifyCodeDTO request) {
        return ApiResponse.onSuccess(MemberConverter.toVerifyCodeResultDTO(
                authCommandService.verifyCode(request)
        ));
    }
}
