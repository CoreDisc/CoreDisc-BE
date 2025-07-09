package com.coredisc.common.converter;

import com.coredisc.domain.Member;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;

import java.time.LocalDateTime;

public class MemberConverter {

    private MemberConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static Member toMember(AuthRequestDTO.SignupDTO request) {

        return Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(request.getNickname())
                .username(request.getUsername())
                .password(request.getPassword())
                .status(true)
                .isSocialLogin(false)
                .oauthType(null)
                .oauthKey(null)
                .build();
    }

    public static AuthResponseDTO.SignupResultDTO toSignupResultDTO(Member member) {

        return AuthResponseDTO.SignupResultDTO.builder()
                .id(member.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
