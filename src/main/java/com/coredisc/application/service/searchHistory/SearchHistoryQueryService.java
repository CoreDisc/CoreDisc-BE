package com.coredisc.application.service.searchHistory;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.searchHistory.SearchHistoryResponseDTO;

import java.time.LocalDateTime;

public interface SearchHistoryQueryService {

    CursorDTO<SearchHistoryResponseDTO.MySearchHistoryResultDTO> getMemberSearchHistoryList(Member member, LocalDateTime cursorSearchedAt, int pageSize);
}
