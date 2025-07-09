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

    @PostMapping("/signup")
    public ApiResponse<AuthResponseDTO.SignupResultDTO> signup(@Valid @RequestBody AuthRequestDTO.SignupDTO request) {

        return ApiResponse.onSuccess(MemberConverter.toSignupResultDTO(
                authCommandService.signup(request)
        ));
    }

    @GetMapping("/check-username")
    public ApiResponse<AuthResponseDTO.CheckUsernameResultDTO> checkUsername(@RequestParam String username) {
        return null;
    }

    @GetMapping("/check-email")
    public ApiResponse<AuthResponseDTO.CheckEmailResultDTO> checkEmail(@RequestParam String email) {
        return null;
    }

    @GetMapping("/check-nickname")
    public ApiResponse<AuthResponseDTO.CheckNicknameResultDTO> checkNickname(@RequestParam String nickname) {
        return null;
    }

    @PostMapping("/send-code")
    public ApiResponse<String> sendCode(@RequestBody @Valid AuthRequestDTO.VerifyEmailDTO request) {
        return null;
    }

    @PostMapping("/verify-code")
    public ApiResponse<AuthResponseDTO.VerifyCodeResultDTO> verifyCode(@RequestBody @Valid AuthRequestDTO.VerifyCodeDTO request) {
        return null;
    }
}
