package com.coredisc.application.service.terms;

import com.coredisc.domain.terms.Terms;

import java.util.List;

public interface TermsQueryService {

    // 이용 약관 리스트 조회
    List<Terms> getTermsList();
}
