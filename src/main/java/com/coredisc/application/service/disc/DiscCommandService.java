package com.coredisc.application.service.disc;

import com.coredisc.domain.common.enums.DiscCoverColor;
import com.coredisc.domain.disc.Disc;
import com.coredisc.domain.member.Member;

public interface DiscCommandService {

    // 나의 디스크 커버 이미지 변경
    Disc updateDiscCoverImage(Long discId, String coverImageUrl, Member member);

    // 나의 디스크 커버 색깔 변경
    Disc updateDiscCoverColor(Long discId, DiscCoverColor coverColor, Member member);

}