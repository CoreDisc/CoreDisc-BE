package com.coredisc.application.service.member;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.common.util.RedisUtil;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.presentation.dto.member.MemberRequestDTO;
import com.coredisc.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtil redisUtil;
    private final JwtProvider jwtProvider;

    @Override
    public void resetPassword(MemberRequestDTO.ResetPasswordDTO request) {

        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (!request.getNewPassword().equals(request.getPasswordCheck())) {
            throw new AuthHandler(ErrorStatus.PASSWORD_NOT_EQUAL);
        }

        member.encodePassword(passwordEncoder.encode(request.getNewPassword()));
        memberRepository.save(member);
    }

    @Override
    public boolean resetNicknameAndUsername(String accessToken, Member member,
                                            MemberRequestDTO.ResetNicknameAndUsernameDTO request) {

        boolean isUsernameChanged = false;

        // 아이디 변경
        if(!member.getUsername().equals(request.getNewUsername())) {
            if(memberRepository.existsByUsername(request.getNewUsername())){
                throw new MemberHandler(ErrorStatus.USERNAME_ALREADY_EXISTS);
            }
            member.setUsername(request.getNewUsername());
            isUsernameChanged = true;
        }

        // 닉네임 변경
        if(!member.getNickname().equals(request.getNewNickname())) {
            if(memberRepository.existsByNickname(request.getNewNickname())) {
                throw new MemberHandler(ErrorStatus.NICKNAME_ALREADY_EXISTS);
            }
            member.setNickname(request.getNewNickname());
        }

        memberRepository.save(member);

        // username 변경시 기존 accessToken 블랙리스트 저장
        if (isUsernameChanged) {
            if(accessToken.startsWith("Bear ")) {
                accessToken = accessToken.substring(7);
            }
            redisUtil.set(accessToken, "logout");
            redisUtil.expire(accessToken, jwtProvider.getRemainingExpiration(accessToken), TimeUnit.MILLISECONDS);
            // RefreshToken 삭제
            redisUtil.delete(String.valueOf(jwtProvider.getId(accessToken)));
        }

        return isUsernameChanged;
    }

    @Override
    public void resignMember(Member member) {

        member.setStatus(Boolean.FALSE);
        memberRepository.save(member);
    }
}
