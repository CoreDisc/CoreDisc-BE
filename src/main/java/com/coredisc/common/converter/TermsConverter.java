package com.coredisc.common.converter;

import com.coredisc.domain.terms.Terms;
import com.coredisc.presentation.dto.terms.TermsResponseDTO;

public class TermsConverter {

    private TermsConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static TermsResponseDTO.TermsDTO toTermsListDTO(Terms terms) {

        return TermsResponseDTO.TermsDTO.builder()
                .termsId(terms.getId())
                .title(terms.getType().getTitle())
                .content(terms.getContent())
                .isRequired(terms.getIsRequired())
                .build();
    }

}
