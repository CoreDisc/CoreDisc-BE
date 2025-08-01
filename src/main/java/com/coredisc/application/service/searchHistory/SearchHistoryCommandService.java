package com.coredisc.application.service.searchHistory;

import com.coredisc.domain.member.Member;

public interface SearchHistoryCommandService {

    // 검색 화면 검색 기록 삭제
    void deleteSearchHistory(Member member, Long historyId);

}
