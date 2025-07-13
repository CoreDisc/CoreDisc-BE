package com.coredisc.common.converter;

import com.coredisc.domain.mapping.MemberTerms;
import com.coredisc.domain.terms.Terms;

import java.util.List;

public class MemberTermsConverter {

    private MemberTermsConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<MemberTerms> toMemberTermsList(List<Terms> termsList) {

        return termsList.stream()
                .map(terms -> MemberTerms.builder()
                        .terms(terms)
                        .build()
                ).toList();
    }
}
