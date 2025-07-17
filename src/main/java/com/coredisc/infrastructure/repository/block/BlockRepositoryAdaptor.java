package com.coredisc.infrastructure.repository.block;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.block.BlockRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlockRepositoryAdaptor implements BlockRepository {

    private final JpaBlockRepository jpaBlockRepository;

    @Override
    public boolean existsByBlockerAndBlocked(Member blocker, Member blocked) {
        return jpaBlockRepository.existsByBlockerAndBlocked(blocker, blocked);
    }

    @Override
    public Block save(Block block) {
        return jpaBlockRepository.save(block);
    }

    @Override
    public void delete(Block block) {
        jpaBlockRepository.delete(block);
    }

    @Override
    public Block findByBlockerAndBlocked(Member blocker, Member blocked) {
        return jpaBlockRepository.findByBlockerAndBlocked(blocker, blocked);
    }
}
