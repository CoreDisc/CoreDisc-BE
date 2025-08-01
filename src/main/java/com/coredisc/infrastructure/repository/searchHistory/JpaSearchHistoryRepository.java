package com.coredisc.infrastructure.repository.searchHistory;

import com.coredisc.domain.common.enums.SearchType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.searchHistory.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaSearchHistoryRepository extends JpaRepository<SearchHistory, Long> {

    Optional<SearchHistory> findByMemberAndKeywordAndSearchType(Member member, String keyword, SearchType searchType);

}
