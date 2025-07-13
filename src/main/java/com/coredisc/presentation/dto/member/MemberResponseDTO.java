package com.coredisc.presentation.dto.member;

import com.coredisc.domain.common.enums.PublicityType;
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
    public static class MyHomeInfoDTO {

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
    public static class UserHomeInfoDTO {

        private Long memberId;

        private String nickname;

        private String joinDate;

        private Long followerCount;

        private Long followingCount;

        private Long discCount;

        private Boolean isFollowing;

        private ProfileImgResponseDTO.ProfileImgDTO profileImgDTO;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyHomeImageAnswerDTO {

        private Long postAnswerImageId;

        private Long postId;

        private String imgUrl;

        private PublicityType publicityType;
    }
}
