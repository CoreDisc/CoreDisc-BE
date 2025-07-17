package com.coredisc.presentation.dto.disc;

import com.coredisc.domain.common.enums.DiscCoverColor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class DiscRequestDTO {

    @Getter
    public static class UpdateCoverImgDTO {
        @NotBlank
        private String coverImageUrl;
    }

    @Getter
    public static class UpdateCoverColorDTO{
        @NotNull
        private DiscCoverColor coverColor;
    }
}