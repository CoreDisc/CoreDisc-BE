package com.coredisc.domain.block;

import com.coredisc.domain.member.Member;

import java.util.List;

public interface BlockRepository {

    // 차단 여부
    boolean existsByBlockerAndBlocked(Member blocker, Member blocked);

    // 차단 정보 저장
    Block save(Block block);

    // 차단 정보 삭제
    void delete(Block block);

    Block findByBlockerAndBlocked(Member blocker, Member blocked);

    List<Block> findAllByBlocked(Member member);
}
