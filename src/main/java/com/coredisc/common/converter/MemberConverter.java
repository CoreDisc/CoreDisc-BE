package com.coredisc.common.converter;

import com.coredisc.common.util.RandomNicknameGenerator;
import com.coredisc.domain.common.enums.Role;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MemberConverter {

    private MemberConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static Member toMember(AuthRequestDTO.SignupDTO request) {

        return Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(RandomNicknameGenerator.generateRandomNickname())
                .username(request.getUsername())
                .password(request.getPassword())
                .status(true)
                .isSocialLogin(false)
                .oauthType(null)
                .oauthKey(null)
                .role(Role.USER)
                .memberTermsList(new ArrayList<>())
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
    public static AuthResponseDTO.CheckUsernameResultDTO toCheckUsernameResultDTO(boolean isDuplicated) {

        return AuthResponseDTO.CheckUsernameResultDTO.builder()
                .isDuplicated(isDuplicated)
                .build();
    }

    public static AuthResponseDTO.CheckEmailResultDTO toCheckEmailResultDTO(boolean isDuplicated) {

        return AuthResponseDTO.CheckEmailResultDTO.builder()
                .isDuplicated(isDuplicated)
                .build();
    }

    public static AuthResponseDTO.CheckNicknameResultDTO toCheckNicknameResultDTO(boolean isDuplicated) {

        return AuthResponseDTO.CheckNicknameResultDTO.builder()
                .isDuplicated(isDuplicated)
                .build();
    }

    public static AuthResponseDTO.FindUsernameResultDTO toFindUsernameResultDTO(Member member) {

        return AuthResponseDTO.FindUsernameResultDTO.builder()
                .username(member.getUsername())
                .build();
    }

    public static MemberResponseDTO.MyHomeUserInfoDTO toMyHomeUserInfoDTO(Member member, Long follwerCount,
                                                                          Long followingCount, ProfileImg profileImg) {

        // 가입 시기 M.d.yyyy 형태로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M.d.yyyy");
        String formattedDate = member.getCreatedAt().format(formatter);

        return MemberResponseDTO.MyHomeUserInfoDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .joinDate(formattedDate)
                .followerCount(follwerCount)
                .followingCount(followingCount)
                // TODO: 총 디스크 수
//                .discCount(discCount)
                .profileImgDTO(ProfileImgConverter.toProfileImgDTO(profileImg))
                .build();
    }
}
