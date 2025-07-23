package com.coredisc.infrastructure.repository.block.queryDsl;

import com.coredisc.domain.block.Block;
import com.coredisc.domain.block.QBlock;
import com.coredisc.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryBlockRepositoryImpl implements QueryBlockRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Block> findBlockedsByBlocker(Member member, Long cursorId, Pageable pageable) {
        QBlock block = QBlock.block;

        return queryFactory
                .selectFrom(block)
                .where(
                        block.blocker.eq(member),
                        cursorId != null ? block.id.lt(cursorId) : null
                )
                .orderBy(block.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }
}
