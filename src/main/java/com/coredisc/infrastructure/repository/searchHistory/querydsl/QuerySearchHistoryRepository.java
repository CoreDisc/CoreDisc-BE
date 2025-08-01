package com.coredisc.infrastructure.repository.searchHistory.querydsl;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.searchHistory.SearchHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface QuerySearchHistoryRepository {

    List<SearchHistory> findAllByMember(Member member, LocalDateTime cursorSearchedAt, int pageSize);
}
