package com.coredisc.application.service.searchHistory;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.SearchHistoryHandler;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.searchHistory.SearchHistory;
import com.coredisc.domain.searchHistory.SearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchHistoryCommandServiceImpl implements SearchHistoryCommandService {


    private final SearchHistoryRepository searchHistoryRepository;

    // 검색 화면 검색 기록 삭제
    @Override
    public void deleteSearchHistory(Member member, Long historyId) {

        SearchHistory searchHistory = searchHistoryRepository.findByMemberAndId(member, historyId)
                .orElseThrow(() -> new SearchHistoryHandler(ErrorStatus.SEARCH_HISTORY_NOT_FOUND));

        searchHistoryRepository.delete(searchHistory);
    }
}
