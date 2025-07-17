package com.coredisc.infrastructure.repository.block;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaBlockRepository extends JpaRepository<Block, Long> {

    boolean existsByBlockerAndBlocked(Member blocker, Member blocked);

    Block findByBlockerAndBlocked(Member blocker, Member blocked);

    List<Block> findAllByBlocker(Member blocked);
}
