package com.coredisc.application.service.member;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.member.MemberRequestDTO;

public interface MemberCommandService {

    void resetPassword(MemberRequestDTO.ResetPasswordDTO request);

    void resetNickname(Member member,MemberRequestDTO.ResetNicknameDTO request);
}
