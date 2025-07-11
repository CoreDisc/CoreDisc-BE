package com.coredisc.application.service.member;

import com.coredisc.presentation.dto.member.MemberRequestDTO;

public interface MemberCommandService {

    void resetPassword(MemberRequestDTO.ResetPasswordDTO request);
}
