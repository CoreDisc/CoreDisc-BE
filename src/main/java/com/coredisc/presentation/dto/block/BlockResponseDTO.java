package com.coredisc.presentation.dto.block;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class BlockResponseDTO {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BlockResultDTO {
        private Long id;
        private Long blockerId;
        private Long blockedId;
        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BlockedDTO {
        private Long blockedId;
        public String blockedNickname;
        public String blockedUsername;
        public String blockedImageUrl;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BlockedListViewDTO {
        private int totalBlockedCount;
        private List<BlockedDTO> blockedList;
    }
}
