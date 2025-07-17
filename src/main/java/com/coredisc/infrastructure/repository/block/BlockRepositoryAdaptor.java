package com.coredisc.infrastructure.repository.block;

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
}
