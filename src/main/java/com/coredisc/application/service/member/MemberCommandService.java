package com.coredisc.application.service.member;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.member.MemberRequestDTO;
import com.coredisc.presentation.dto.profileImg.ProfileImgResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MemberCommandService {

    // 비밀번호 변경
    void resetPassword(MemberRequestDTO.ResetPasswordDTO request);

    // 마이 홈 - 닉네임, 아이디 변경
    boolean resetNicknameAndUsernameMyHome(String accessToken, Member member, String deviceToken,
                                           MemberRequestDTO.MyHomeResetNicknameAndUsernameDTO request);

    // 계정 탈퇴
    void resignMember(Member member);

    // 계정 관리 - 이메일 변경
    void resetEmailMyHome(Member member, MemberRequestDTO.MyHomeResetEmailDTO request);

    // 계정 관리 - 비밀번호 변경
    void resetPasswordMyHome(Member member, MemberRequestDTO.MyHomeResetPasswordDTO request);

    // 계정 관리 - 아이디 변경
    void resetUsernameMyHome(String accessToken, Member member, String deviceToken, MemberRequestDTO.MyHomeResetUsernameDTO request);

    // 프로필 사진 변경
    ProfileImgResponseDTO.ProfileImgDTO resetProfileImg(Member member, MultipartFile newProfileImg);

    // 기본 프로필 사진으로 변경
    ProfileImgResponseDTO.ProfileImgDTO resetToDefaultProfileImg(Member member);
}
