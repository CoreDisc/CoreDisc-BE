package com.coredisc.presentation.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FollowResponseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowerDTO {
        private Long followerId;
        private String followerNickname;
        private String followerUsername;
        private String followerImageUrl;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowerListViewDTO {
        private List<FollowerDTO> followers;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowingDTO {
        private Long followingId;
        private String followingNickname;
        private String followingUsername;
        private String followingImageUrl;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowingListViewDTO {
        private List<FollowingDTO> followings;
    }

}
