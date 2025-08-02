package com.coredisc.presentation.dto.follow;

import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.profileImg.ProfileImgResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FollowResponseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowerDTO {
        private Long followerId;
        private String nickname;
        private String username;
        private ProfileImgResponseDTO.ProfileImgDTO profileImgDTO;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean isCircle;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Boolean isMutual;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowerListDTO {
        private int totalCount;
        private CursorDTO<FollowerDTO> followerCursor;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowingDTO {
        private Long followingId;
        private String nickname;
        private String username;
        private ProfileImgResponseDTO.ProfileImgDTO profileImgDTO;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FollowingListDTO {
        private int totalCount;
        private CursorDTO<FollowingDTO> followingCursor;
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
