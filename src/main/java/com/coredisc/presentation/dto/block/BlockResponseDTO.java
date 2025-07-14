package com.coredisc.presentation.dto.block;

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
        private LocalDateTime createdAt;
    }
}
