package com.coredisc.presentation.dto.member;

import com.coredisc.presentation.dto.profileImg.ProfileImgResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyHomeInfoOfMeDTO {

        private Long memberId;

        private String nickname;

        private String joinDate;

        private Long followerCount;

        private Long followingCount;

        private Long discCount;

        private ProfileImgResponseDTO.ProfileImgDTO profileImgDTO;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyHomeInfoOfOtherDTO {

        private Long memberId;

        private String nickname;

        private String joinDate;

        private Long followerCount;

        private Long followingCount;

        private Long discCount;

        private Boolean isFollowing;

        private ProfileImgResponseDTO.ProfileImgDTO profileImgDTO;
    }
}
