package com.coredisc.application.service.member;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.member.MemberRequestDTO;

public interface MemberCommandService {

    // 비밀번호 변경
    void resetPassword(MemberRequestDTO.ResetPasswordDTO request);

    // 닉네임, 아이디 변경
    boolean resetNicknameAndUsername(String accessToken, Member member,
                                     MemberRequestDTO.ResetNicknameAndUsernameDTO request);

    // 계정 탈퇴
    void resignMember(Member member);
}
