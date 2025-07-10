package com.coredisc.application.service.auth;

import com.coredisc.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthQueryServiceImpl implements AuthQueryService{

    private final MemberRepository memberRepository;

    // username 중복 검사
    @Override
    public boolean checkUsername(String username) {
        return memberRepository.existsByUsername(username);
    }

    // email 중복 검사
    @Override
    public boolean checkEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    // nickname 중복 검사
    @Override
    public boolean checkNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }
}
