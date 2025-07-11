package com.coredisc.application.service.member;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.member.MemberRequestDTO;

public interface MemberCommandService {

    // 비밀번호 변경
    void resetPassword(MemberRequestDTO.ResetPasswordDTO request);

    // 닉네임 변경
    void resetNickname(Member member, MemberRequestDTO.ResetNicknameDTO request);

    // 계정 탈퇴
    void resignMember(Member member);
}
