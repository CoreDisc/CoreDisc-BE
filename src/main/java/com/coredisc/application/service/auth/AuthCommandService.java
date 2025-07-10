package com.coredisc.application.service.auth;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;

public interface AuthCommandService {

    // 회원가입
    Member signup(AuthRequestDTO.SignupDTO request);

    // 이메일 코드 전송
    void sendCode(AuthRequestDTO.VerifyEmailDTO request);

    // 코드 인증
    boolean verifyCode(AuthRequestDTO.VerifyCodeDTO request);
}
