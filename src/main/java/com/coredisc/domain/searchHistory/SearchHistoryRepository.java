package com.coredisc.domain.searchHistory;

import com.coredisc.domain.common.enums.SearchType;
import com.coredisc.domain.member.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SearchHistoryRepository {

    Optional<SearchHistory> findByMemberAndKeywordAndSearchType(Member member, String keyword, SearchType searchType);

    SearchHistory save(SearchHistory searchHistory);

    List<SearchHistory> findAllByMember(Member member, LocalDateTime cursorSearchedAt, int pageSize);

}
