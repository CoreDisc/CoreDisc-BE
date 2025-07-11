package com.coredisc.presentation.dto.follow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
        private boolean isCircle;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowerListViewDTO {
        private int totalFollowerCount;
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
        private boolean isCircle;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowingListViewDTO {
        private int totalFollowingCount;
        private List<FollowingDTO> followings;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowResultDTO {
        private Long id;
        private Long followerId;
        private Long followingId;
        private LocalDateTime createdAt;
    }

}
