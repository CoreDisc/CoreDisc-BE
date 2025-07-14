package com.coredisc.application.service.block;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.member.Member;

public interface BlockCommandService {

    Block block(Member member, Long targetId);
    void unblock(Member member, Long targetId);
}
