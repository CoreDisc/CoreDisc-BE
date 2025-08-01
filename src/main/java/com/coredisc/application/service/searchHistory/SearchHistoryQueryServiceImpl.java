package com.coredisc.application.service.searchHistory;

import com.coredisc.common.converter.SearchHistoryConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.searchHistory.SearchHistory;
import com.coredisc.domain.searchHistory.SearchHistoryRepository;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.searchHistory.SearchHistoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchHistoryQueryServiceImpl implements SearchHistoryQueryService {

    private final SearchHistoryRepository searchHistoryRepository;

    @Override
    public CursorDTO<SearchHistoryResponseDTO.MySearchHistoryResultDTO> getMemberSearchHistoryList(Member member, LocalDateTime cursorSearchedAt, int pageSize) {


        List<SearchHistory> searchHistoryList = searchHistoryRepository.findAllByMember(member, cursorSearchedAt, pageSize);

        boolean hasNext = searchHistoryList.size() > pageSize;

        if (hasNext) {
            searchHistoryList = searchHistoryList.subList(0, pageSize);
        }

        List<SearchHistoryResponseDTO.MySearchHistoryResultDTO> mySearchHistoryList = searchHistoryList.stream()
                .map(SearchHistoryConverter::toMySearchHistoryResultDTO)
                .toList();

        return new CursorDTO<>(mySearchHistoryList, hasNext);
    }

}
