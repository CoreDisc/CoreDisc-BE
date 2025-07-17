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

        private String username;

        private String nickname;

        private String followerCount;

        private String followingCount;

        private String discCount;

        private ProfileImgResponseDTO.ProfileImgDTO profileImgDTO;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserHomeInfoDTO {

        private Long memberId;

        private String username;

        private String nickname;

        private String followerCount;

        private String followingCount;

        private String discCount;

        private Boolean isFollowing;

        private boolean isBlocked;

        private ProfileImgResponseDTO.ProfileImgDTO profileImgDTO;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MyHomePostDTO {

        private Long postId;

        private PublicityType publicityType;

        private PostImageThumbnailDTO postImageThumbnailDTO; // 4개의 답변 중 이미지 답변이 존재할 경우

        private PostTextThumbnailDTO postTextThumbnailDTO; // 4개의 답변 모두 텍스트 답변일 경우
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserHomePostDTO {

        private Long postId;

        private PostImageThumbnailDTO postImageThumbnailDTO; // 4개의 답변 중 이미지 답변이 존재할 경우

        private PostTextThumbnailDTO postTextThumbnailDTO; // 4개의 답변 모두 텍스트 답변일 경우
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostImageThumbnailDTO {

        private String thumbnailUrl;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PostTextThumbnailDTO {

        private String weekday;

        private String createdAt;

        // TODO: 답변의 질문 카테고리
//        private List<String> categories;
    }

}
