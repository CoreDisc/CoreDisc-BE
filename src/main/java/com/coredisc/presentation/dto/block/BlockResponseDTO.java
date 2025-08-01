package com.coredisc.presentation.dto.block;

import com.coredisc.presentation.dto.profileImg.ProfileImgResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class BlockResponseDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BlockResultDTO {
        private Long id;
        private Long blockerId;
        private Long blockedId;
        private ProfileImgResponseDTO.ProfileImgDTO profileImgDTO;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BlockedDTO {
        private Long blockedId;
        private String blockedNickname;
        private String blockedUsername;
        private ProfileImgResponseDTO.ProfileImgDTO profileImgDTO;
    }

}
