package com.coredisc.application.service.auth;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.util.RedisUtil;
import com.coredisc.domain.common.enums.EmailRequestType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import com.coredisc.presentation.dto.jwt.JwtDTO;
import com.coredisc.security.auth.PrincipalDetails;
import com.coredisc.security.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final RedisUtil redisUtil;
    private final JwtProvider jwtProvider;

    // 회원가입
    @Override
    public Member signup(AuthRequestDTO.SignupDTO request) {

        if(!request.getPassword().equals(request.getPasswordCheck())) {
            throw new AuthHandler(ErrorStatus.PASSWORD_NOT_EQUAL);
        }
        // 1차 방어
        if(memberRepository.existsByUsername(request.getUsername())) {
            throw new AuthHandler(ErrorStatus.USERNAME_ALREADY_EXISTS);
        }
        if(memberRepository.existsByEmail(request.getEmail())) {
            throw new AuthHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
        }
        if(memberRepository.existsByNickname(request.getNickname())) {
            throw new AuthHandler(ErrorStatus.NICKNAME_ALREADY_EXISTS);
        }

        Member newMember = MemberConverter.toMember(request);
        newMember.encodePassword(passwordEncoder.encode(request.getPassword()));

        try {
            return memberRepository.save(newMember);
        } catch (DataIntegrityViolationException e) { // race condition 이중 방어
            throw new AuthHandler(ErrorStatus.DUPLICATED_RESOURCE);
        }

    }

    // 이메일 코드 전송
    @Override
    public void sendCode(AuthRequestDTO.VerifyEmailDTO request, EmailRequestType emailRequestType) {

        try {
            mailService.sendEmail(request.getEmail(), emailRequestType);
        } catch (MessagingException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_WRITE_FAILED);
        } catch (MailException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_SEND_FAILED);
        }
    }

    // 코드 인증
    @Override
    public boolean verifyCode(AuthRequestDTO.VerifyCodeDTO request) {

        String authCode = (String) redisUtil.get("auth:" + request.getUsername() + ":" + request.getEmailRequestType());

        if (authCode == null) {
            throw new AuthHandler(ErrorStatus.EMAIL_CODE_EXPIRED);
        }
        if (!authCode.equals(request.getCode())) {
            throw new AuthHandler(ErrorStatus.CODE_NOT_EQUAL);
        }
        return true;
    }

    // 로그인
    @Override
    public AuthResponseDTO.LoginResultDTO login(AuthRequestDTO.LoginDTO request) {

        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new AuthHandler(ErrorStatus.PASSWORD_NOT_EQUAL);
        }

        PrincipalDetails principalDetails = new PrincipalDetails(member);

        // 로그인 성공 시 토큰 생성
        String accessToken = jwtProvider.createAccessToken(principalDetails, member.getId());
        String refreshToken = jwtProvider.createRefreshToken(principalDetails, member.getId());

        return MemberConverter.toLoginResultDTO(member, accessToken, refreshToken);
    }

    // 토큰 재발급
    @Override
    public JwtDTO reissueToken(String refreshToken) {

        try {
            jwtProvider.validateRefreshToken(refreshToken);

            return jwtProvider.reissueToken(refreshToken);
        } catch (ExpiredJwtException eje) {
            throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
        } catch (IllegalArgumentException iae) {
            throw new AuthHandler(ErrorStatus.INVALID_TOKEN);
        }
    }

    @Override
    public void logout(HttpServletRequest request) {

        try {
            String accessToken = jwtProvider.resolveAccessToken(request);

            // 블랙리스트에 저장
            redisUtil.set(accessToken, "logout");
            redisUtil.expire(accessToken, jwtProvider.getExpTime(accessToken), TimeUnit.MILLISECONDS);
            // RefreshToken 삭제
            redisUtil.delete(jwtProvider.getUsername(accessToken));
        } catch (ExpiredJwtException e) {
            throw new AuthHandler(ErrorStatus.TOKEN_EXPIRED);
        }
    }
}
