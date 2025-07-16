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
                // ex) 100 -> 1, 201 -> 2.01로 변환
                .version(terms.getVersion() / 100 + (terms.getVersion() % 100) / 100.0)
                .isRequired(terms.getIsRequired())
                .build();
    }

}
