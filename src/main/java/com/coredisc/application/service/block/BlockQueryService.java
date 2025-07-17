package com.coredisc.application.service.block;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.member.Member;

import java.util.List;

public interface BlockQueryService {

    // 차단한 유저 목록 조회
    List<Block> getBlockeds(Member member);
}
