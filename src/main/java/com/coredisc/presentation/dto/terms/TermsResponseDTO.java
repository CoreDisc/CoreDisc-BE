package com.coredisc.presentation.dto.terms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TermsResponseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TermsDTO {

        private Long termsId;

        private String title;

        private String content;

        private Double version;

        private Boolean isRequired;
    }
}
