package com.coredisc.common.converter;

import com.coredisc.domain.common.enums.SearchType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.searchHistory.SearchHistory;
import com.coredisc.presentation.dto.searchHistory.SearchHistoryResponseDTO;

import java.time.LocalDateTime;

public class SearchHistoryConverter {

    public static SearchHistory toSearchHistory(Member member, String keyword, SearchType searchType) {
        return SearchHistory.builder()
                .member(member)
                .keyword(keyword)
                .searchedAt(LocalDateTime.now())
                .searchType(searchType)
                .build();
    }

    public static SearchHistoryResponseDTO.MySearchHistoryResultDTO toMySearchHistoryResultDTO(SearchHistory searchHistory) {

        return SearchHistoryResponseDTO.MySearchHistoryResultDTO.builder()
                .id(searchHistory.getId())
                .keyword(searchHistory.getKeyword())
                .searchedAt(searchHistory.getSearchedAt())
                .build();
    }
}
