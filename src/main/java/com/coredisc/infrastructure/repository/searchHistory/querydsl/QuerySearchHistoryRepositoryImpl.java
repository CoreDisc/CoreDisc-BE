package com.coredisc.infrastructure.repository.searchHistory.querydsl;

import com.coredisc.domain.common.enums.SearchType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.searchHistory.QSearchHistory;
import com.coredisc.domain.searchHistory.SearchHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QuerySearchHistoryRepositoryImpl implements QuerySearchHistoryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final QSearchHistory qSearchHistory = QSearchHistory.searchHistory;

    @Override
    public List<SearchHistory> findAllByMember(Member member, LocalDateTime cursorSearchedAt, int pageSize) {

        return jpaQueryFactory
                .selectFrom(qSearchHistory)
                .where(
                        qSearchHistory.member.eq(member),
                        qSearchHistory.searchType.eq(SearchType.MEMBER),
                        cursorSearchedAt != null ? qSearchHistory.searchedAt.lt(cursorSearchedAt) : null
                )
                .orderBy(qSearchHistory.searchedAt.desc())
                .limit(pageSize + 1)
                .fetch();
    }

}
