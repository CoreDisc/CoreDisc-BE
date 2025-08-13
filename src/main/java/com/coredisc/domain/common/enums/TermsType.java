package com.coredisc.domain.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TermsType {

    // 필수 항목
    SERVICE_USE_REQUIRED("서비스 이용약관"),
    PRIVACY_REQUIRED("개인정보 수집 및 이용 동의"),
    AGE_OVER_14("만 14세 이상 여부"),

    // 선택 항목
    MARKETING_AGREEMENT("마케팅 활용 및 광고 수신 동의"),
    THIRD_PARTY_PROVIDE("개인정보 제3자 제공 동의"),
    OUTSOURCING_AGREEMENT("개인정보 처리 위탁 동의");

    private final String title;
}
