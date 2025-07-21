package com.coredisc.infrastructure.repository.follow.queryDSL;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.follow.QFollow;
import com.coredisc.domain.member.Member;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QueryFollowRepositoryImpl implements QueryFollowRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Follow> findCircleFollowers(Member member, Long cursorId, Pageable pageable) {
        QFollow follow = QFollow.follow;

        return queryFactory
                .selectFrom(follow)
                .where(
                        follow.following.eq(member),
                        follow.isCircle.isTrue(),
                        cursorId != null ? follow.id.lt(cursorId) : null
                )
                .orderBy(follow.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    @Override
    public int countCircleFollowers(Member member) {
        QFollow follow = QFollow.follow;

        Long count = queryFactory
                .select(follow.count())
                .from(follow)
                .where(
                        follow.following.eq(member),
                        follow.isCircle.isTrue()
                )
                .fetchOne();

        return count != null ? count.intValue() : 0;
    }
}
