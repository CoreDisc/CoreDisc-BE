package com.coredisc.infrastructure.repository.block.queryDsl;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryBlockRepository {

    List<Block> findBlockedsByBlocker(Member member, Long cursorId, Pageable pageable);
}
