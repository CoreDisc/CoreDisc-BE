package com.coredisc.infrastructure.repository.searchHistory;

import com.coredisc.domain.common.enums.SearchType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.searchHistory.SearchHistory;
import com.coredisc.domain.searchHistory.SearchHistoryRepository;
import com.coredisc.infrastructure.repository.searchHistory.querydsl.QuerySearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SearchHistoryRepositoryAdapter implements SearchHistoryRepository {

    private final JpaSearchHistoryRepository jpaSearchHistoryRepository;
    private final QuerySearchHistoryRepository querySearchHistoryRepository;

    @Override
    public Optional<SearchHistory> findByMemberAndKeywordAndSearchType(Member member, String keyword, SearchType searchType) {
        return jpaSearchHistoryRepository.findByMemberAndKeywordAndSearchType(member, keyword, searchType);
    }

    @Override
    public SearchHistory save(SearchHistory searchHistory) {
        return jpaSearchHistoryRepository.save(searchHistory);
    }

    @Override
    public List<SearchHistory> findAllByMember(Member member, LocalDateTime cursorSearchedAt, int pageSize) {
        return querySearchHistoryRepository.findAllByMember(member, cursorSearchedAt, pageSize);
    }

}
