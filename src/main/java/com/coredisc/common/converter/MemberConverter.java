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

    public static AuthResponseDTO.VerifyCodeResultDTO toVerifyCodeResultDTO(boolean isVerified) {

        return AuthResponseDTO.VerifyCodeResultDTO.builder()
                .isVerified(isVerified)
                .build();
    }

    public static AuthResponseDTO.LoginResultDTO toLoginResultDTO(Member member, String accessToken, String refreshToken) {

        return AuthResponseDTO.LoginResultDTO.builder()
                .id(member.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
