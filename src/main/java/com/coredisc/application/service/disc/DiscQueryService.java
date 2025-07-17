package com.coredisc.application.service.disc;

import com.coredisc.domain.disc.Disc;
import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DiscQueryService {

    // 나의 디스크 목록 조회
    Page<Disc> getMyDiscList(Member member, Pageable pageable);

    // 나의 디스크 조회
    Disc getDisc(Long discId, Member member);
}
