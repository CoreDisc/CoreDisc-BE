package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Auth", description = "회원가입 관련 API")
public interface AuthControllerDocs {

    @Operation(summary = "회원가입", description = "회원가입 기능입니다.")
    ApiResponse<AuthResponseDTO.SignupResultDTO> signup(@Valid @RequestBody AuthRequestDTO.SignupDTO request);

    @Operation(summary = "아이디 중복 확인", description = "아이디 중복 확인 기능입니다.")
    @Parameter(name = "username", description = "로그인 아이디")
    ApiResponse<AuthResponseDTO.CheckUsernameResultDTO> checkUsername(@RequestParam String username);

    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인 기능입니다.")
    @Parameter(name = "email", description = "이메일")
    ApiResponse<AuthResponseDTO.CheckEmailResultDTO> checkEmail(@RequestParam String email);

    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인 기능입니다.")
    @Parameter(name = "nickname", description = "닉네임")
    ApiResponse<AuthResponseDTO.CheckNicknameResultDTO> checkNickname(@RequestParam String nickname);

    @Operation(summary = "이메일 인증 메일 전송", description = "이메일 인증을 위한 메일 전송 기능입니다.")
    ApiResponse<String> sendCode(@RequestBody @Valid AuthRequestDTO.VerifyEmailDTO request);

    @Operation(summary = "이메일 코드 인증", description = "회원가입 시 이메일 코드 인증 기능입니다.")
    ApiResponse<AuthResponseDTO.VerifyCodeResultDTO> verifyCode(@RequestBody @Valid AuthRequestDTO.VerifyCodeDTO request);

    @Operation(summary = "일반 로그인", description = "일반 로그인 기능입니다.")
    ApiResponse<AuthResponseDTO.LoginResultDTO> login(@RequestBody @Valid AuthRequestDTO.LoginDTO request);
}
