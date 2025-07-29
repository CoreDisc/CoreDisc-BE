package com.coredisc.infrastructure.repository.member.querydsl;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryMemberRepositoryImpl implements QueryMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QMember qMember = QMember.member;

    // 검색 화면 사용자 검색
    @Override
    public List<Member> findMemberListByKeyword(
            String keyword,
            Long cursorId,
            int pageSize
    ) {
        return jpaQueryFactory
                .selectFrom(qMember)
                .where(
                        qMember.username.containsIgnoreCase(keyword)
                                .or(qMember.nickname.containsIgnoreCase(keyword)),
                        cursorId != null ? qMember.id.lt(cursorId) : null
                )
                .orderBy(qMember.id.desc())
                .limit(pageSize + 1)
                .fetch();
    }
}
