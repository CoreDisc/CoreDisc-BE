package com.coredisc.domain.block;

import com.coredisc.domain.member.Member;

public interface BlockRepository {

    // 차단 여부
    boolean existsByBlockerAndBlocked(Member blocker, Member blocked);
}
