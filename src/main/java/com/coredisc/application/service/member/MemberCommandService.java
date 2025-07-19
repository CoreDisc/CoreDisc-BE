package com.coredisc.application.service.member;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.member.MemberRequestDTO;

public interface MemberCommandService {

    // 비밀번호 변경
    void resetPassword(MemberRequestDTO.ResetPasswordDTO request);

    // 마이 홈 - 닉네임, 아이디 변경
    boolean resetNicknameAndUsernameMyHome(String accessToken, Member member,
                                           MemberRequestDTO.MyHomeResetNicknameAndUsernameDTO request);

    // 계정 탈퇴
    void resignMember(Member member);

    // 계정 관리 - 이메일 변경
    void resetEmailMyHome(Member member, MemberRequestDTO.MyHomeResetEmailDTO request);

    // 계정 관리 - 비밀번호 변경
    void resetPasswordMyHome(Member member, MemberRequestDTO.MyHomeResetPasswordDTO request);
}
