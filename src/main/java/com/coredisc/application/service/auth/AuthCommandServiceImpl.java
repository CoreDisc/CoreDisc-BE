package com.coredisc.application.service.auth;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.util.RedisUtil;
import com.coredisc.domain.Member;
import com.coredisc.infrastructure.repository.member.JpaMemberRepository;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final PasswordEncoder passwordEncoder;
    private final JpaMemberRepository memberRepository;
    private final MailService mailService;
    private final RedisUtil redisUtil;

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

        try {
            mailService.sendEmail(request.getEmail());
        } catch (MessagingException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_WRITE_FAILED);
        } catch (MailException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }

    // 코드 인증
    @Override
    public boolean verifyCode(AuthRequestDTO.VerifyCodeDTO request) {

        String authCode = (String) redisUtil.get("auth: " + request.getEmail());

        if (authCode == null) {
            throw new AuthHandler(ErrorStatus.EMAIL_CODE_EXPIRED);
        }
        if (!authCode.equals(request.getCode())) {
            throw new AuthHandler(ErrorStatus.CODE_NOT_EQUAL);
        }
        return true;
    }
}
