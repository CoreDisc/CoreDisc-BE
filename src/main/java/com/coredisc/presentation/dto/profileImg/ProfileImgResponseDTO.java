package com.coredisc.presentation.dto.profileImg;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProfileImgResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileImgDTO {

        private Long profileImgId;

        private String imageUrl;
    }
}
