package com.coredisc.application.service.auth;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import com.coredisc.presentation.dto.jwt.JwtDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthCommandService {

    // 회원가입
    Member signup(AuthRequestDTO.SignupDTO request);

    // 이메일 코드 전송
    void sendCode(AuthRequestDTO.VerifyEmailDTO request);

    // 코드 인증
    boolean verifyCode(AuthRequestDTO.VerifyCodeDTO request);

    // 로그인
    AuthResponseDTO.LoginResultDTO login(AuthRequestDTO.LoginDTO request);

    // AccessToken 재발급
    JwtDTO reissueToken(String refreshToken);

    // 로그아웃
    void logout(HttpServletRequest request);
}
