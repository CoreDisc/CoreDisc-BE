package com.coredisc.presentation.dto.disc;

import com.coredisc.domain.common.enums.DiscCoverColor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class DiscResponseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DiscListDTO{
        private int totalDiscCount;
        private List<DiscDTO> discs;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DiscDTO{
        private long id;
        private int year;
        private int month;
        private DiscCoverColor coverColor;
        private boolean hasCoverImage;
        private String coverImageUrl;
    }
}
