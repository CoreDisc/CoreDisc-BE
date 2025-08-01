package com.coredisc.presentation.dto.searchHistory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SearchHistoryResponseDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MySearchHistoryResultDTO {

        private Long id;

        private String keyword;

        private LocalDateTime searchedAt;
    }
}
