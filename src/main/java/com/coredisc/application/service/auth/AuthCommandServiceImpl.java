package com.coredisc.application.service.auth;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.domain.Member;
import com.coredisc.infrastructure.repository.member.JpaMemberRepository;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final PasswordEncoder passwordEncoder;
    private final JpaMemberRepository memberRepository;

    // 회원가입
    @Override
    public Member signup(AuthRequestDTO.SignupDTO request) {

        if(!request.getPassword().equals(request.getPasswordCheck())) {
            throw new AuthHandler(ErrorStatus.PASSWORD_NOT_EQUAL);
        }

        Member newMember = MemberConverter.toMember(request);
        newMember.encodePassword(passwordEncoder.encode(request.getPassword()));

        return memberRepository.save(newMember);
    }

    // 이메일 코드 전송
    @Override
    public void sendCode(AuthRequestDTO.VerifyEmailDTO request) {

    }

    // 코드 인증
    @Override
    public boolean verifyCode(AuthRequestDTO.VerifyCodeDTO request) {
        return true;
    }
}
