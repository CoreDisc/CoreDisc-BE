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

    public static MemberResponseDTO.MyHomeInfoOfMeDTO toMyHomeInfoOfMeDTO(Member member, Long followerCount,
                                                                          Long followingCount, ProfileImg profileImg) {
        // 가입 시기 M.d.yyyy 형태로 변환
        String formattedDate = formatJoinDate(member);

        return MemberResponseDTO.MyHomeInfoOfMeDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .joinDate(formattedDate)
                .followerCount(followerCount)
                .followingCount(followingCount)
                // TODO: 총 디스크 수
//                .discCount(discCount)
                .profileImgDTO(ProfileImgConverter.toProfileImgDTO(profileImg))
                .build();
    }

    public static MemberResponseDTO.MyHomeInfoOfOtherDTO toMyHomeInfoOfOtherDTO(Member targetMember, Long followerCount,
                                                                                Long followingCount, ProfileImg profileImg,
                                                                                Boolean isFollowing) {
        // 가입 시기 M.d.yyyy 형태로 변환
        String formattedDate = formatJoinDate(targetMember);

        return MemberResponseDTO.MyHomeInfoOfOtherDTO.builder()
                .memberId(targetMember.getId())
                .nickname(targetMember.getNickname())
                .joinDate(formattedDate)
                .followerCount(followerCount)
                .followingCount(followingCount)
                // TODO: 총 디스크 수
//                .discCount(discCount)
                .isFollowing(isFollowing)
                .profileImgDTO(ProfileImgConverter.toProfileImgDTO(profileImg))
                .build();
    }

    // 날짜 M.d.yyyy 형태로 변환
    private static String formatJoinDate(Member member) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M.d.yyyy");

        return member.getCreatedAt().format(formatter);
    }
}
