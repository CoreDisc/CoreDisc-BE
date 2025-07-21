package com.coredisc.application.service.block;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.member.Member;

public interface BlockCommandService {

    // 차단하기
    Block block(Member member, Long targetId);

    // 차단 취소하기
    void unblock(Member member, Long targetId);
}
