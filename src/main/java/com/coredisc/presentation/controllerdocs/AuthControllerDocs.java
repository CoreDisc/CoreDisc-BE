package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import com.coredisc.presentation.dto.jwt.JwtDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Auth", description = "회원가입/로그인/로그아웃/인증 관련 API")
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

    @Operation(summary = "토큰 재발급", description = "accessToken이 만료 시 refreshToken을 통해 accessToken을 재발급합니다.")
    ApiResponse<JwtDTO> reissueToken(@RequestHeader("RefreshToken") String refreshToken);

    @Operation(summary = "로그아웃", description = "로그아웃 기능입니다.")
    ApiResponse<String> logout(HttpServletRequest request);

    @Operation(summary = "아이디 찾기", description = "아이디 찾기 기능입니다. 이름과 이메일을 입력합니다.")
    ApiResponse<AuthResponseDTO.FindUsernameResultDTO> findUsername(@RequestBody @Valid AuthRequestDTO.FindUsernameDTO request);

    @Operation(summary = "비밀번호 변경을 위한 사용자 검증", description = "비밀변호 변경을 위해 사용자 검증을 진행합니다. 사용자가 존재하면 인증코드 메일을 보냅니다.")
    ApiResponse<String> verifyUser(@RequestBody @Valid AuthRequestDTO.VerifyUserDTO request);
}
