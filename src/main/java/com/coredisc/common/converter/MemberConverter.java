package com.coredisc.common.converter;

import com.coredisc.domain.common.enums.OauthType;
import com.coredisc.domain.common.enums.Role;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswerImage;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;
import com.coredisc.presentation.dto.auth.KakaoUserInfo;
import com.coredisc.presentation.dto.auth.NaverUserInfo;
import com.coredisc.presentation.dto.member.MemberResponseDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MemberConverter {

    private MemberConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static Member toMember(AuthRequestDTO.SignupDTO request, String nickname) {

        return Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .nickname(nickname)
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

    public static MemberResponseDTO.MyHomeInfoDTO toMyHomeInfoDTO(Member member, String followerCount,
                                                                  String followingCount, String postCount,
                                                                  ProfileImg profileImg) {

        return MemberResponseDTO.MyHomeInfoDTO.builder()
                .memberId(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .postCount(postCount)
                .profileImgDTO(ProfileImgConverter.toProfileImgDTO(profileImg))
                .build();
    }

    public static MemberResponseDTO.UserHomeInfoDTO toUserHomeInfoDTO(Member targetMember, String followerCount,
                                                                      String followingCount, String postCount,
                                                                      ProfileImg profileImg, boolean isFollowing,
                                                                      boolean isBlocked) {

        return MemberResponseDTO.UserHomeInfoDTO.builder()
                .memberId(targetMember.getId())
                .username(targetMember.getUsername())
                .nickname(targetMember.getNickname())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .postCount(postCount)
                .isFollowing(isFollowing)
                .isBlocked(isBlocked)
                .profileImgDTO(ProfileImgConverter.toProfileImgDTO(profileImg))
                .build();
    }

    public static MemberResponseDTO.MyHomePostDTO toMyHomePostDTO(Post post,
                                                                  MemberResponseDTO.PostImageThumbnailDTO imageDto,
                                                                  MemberResponseDTO.PostTextThumbnailDTO textDto) {
        // 이미지 답변이 있는 게시글일 때
        if(imageDto != null) {
            return MemberResponseDTO.MyHomePostDTO.builder()
                    .postId(post.getId())
                    .postImageThumbnailDTO(imageDto)
                    .postTextThumbnailDTO(null)
                    .build();
        }

        // 텍스트 답변만 있는 게시글일 때
        return MemberResponseDTO.MyHomePostDTO.builder()
                .postId(post.getId())
                .postImageThumbnailDTO(null)
                .postTextThumbnailDTO(textDto)
                .build();
    }

    public static MemberResponseDTO.PostImageThumbnailDTO toPostImageThumbnailDTO(PostAnswerImage postAnswerImage) {

        return MemberResponseDTO.PostImageThumbnailDTO.builder()
                .thumbnailUrl(postAnswerImage.getThumbnailUrl())
                .build();
    }

    public static MemberResponseDTO.PostTextThumbnailDTO toPostTextThumbnailDTO(String content) {
        return MemberResponseDTO.PostTextThumbnailDTO.builder()
                .content(content)
                .build();
    }

    public static Member toKakaoMember(KakaoUserInfo kakaoUserInfo, String randomNickname,
                                       String randomUsername, String password) {
        return Member.builder()
                .username(randomUsername)
                .password(password)
                .name(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
                .nickname(randomNickname)
                .email(kakaoUserInfo.getKakaoAccount().getEmail())
                .isSocialLogin(true)
                .oauthType(OauthType.KAKAO)
                .status(true)
                .role(Role.USER)
                .build();
    }

    public static Member toNaverMember(NaverUserInfo naverUserInfo, String randomNickname,
                                       String randomUsername, String password) {
        return Member.builder()
                .username(randomUsername)
                .password(password)
                .name(naverUserInfo.getResponse().getName())
                .nickname(randomNickname)
                .email(naverUserInfo.getResponse().getEmail())
                .isSocialLogin(true)
                .oauthType(OauthType.NAVER)
                .status(true)
                .role(Role.USER)
                .build();
    }

    public static MemberResponseDTO.CheckSocialResultDTO toCheckSocialResultDTO(boolean isSocialLogin) {

        return MemberResponseDTO.CheckSocialResultDTO.builder()
                .isSocialLogin(isSocialLogin)
                .build();
    }

    // 검색 화면 사용자 검색
    public static MemberResponseDTO.SearchMemberResultDTO toSearchMemberResultDTO(Member member, ProfileImg profileImg) {
        return MemberResponseDTO.SearchMemberResultDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .profileImgDTO(ProfileImgConverter.toProfileImgDTO(profileImg))
                .build();
    }
}
