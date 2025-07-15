package com.coredisc.application.service.disc;

import com.coredisc.domain.disc.Disc;
import com.coredisc.domain.member.Member;

import java.util.List;

public interface DiscQueryService {

    // 나의 디스크 목록 조회
    List<Disc> getMyDiscList(Member member);

    // 나의 디스크 조회
    Disc getDisc(Long discId, Member member);
}
