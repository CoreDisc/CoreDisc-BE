package com.coredisc.domain.block;

import com.coredisc.domain.member.Member;

public interface BlockRepository {

    // 차단 여부
    boolean existsByBlockerAndBlocked(Member blocker, Member blocked);

    // 차단 정보 저장
    Block save(Block block);
}
