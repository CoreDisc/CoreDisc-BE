package com.coredisc.application.service.auth;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
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

    // 아이디(username) 찾기
    @Override
    public Member findUsername(AuthRequestDTO.FindUsernameDTO request) {

        return memberRepository.findByNameAndEmail(request.getName(), request.getEmail())
                .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    // 비밀번호 변경을 위한 사용자 검증
    @Override
    public boolean verifyUser(AuthRequestDTO.VerifyUserDTO request) {

        return memberRepository.existsByNameAndUsername(request.getName(), request.getUsername());
    }

    @Override
    public Member findMember(AuthRequestDTO.VerifyUserDTO request) {

        return memberRepository.findByNameAndUsername(request.getName(), request.getUsername())
                .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
