package com.coredisc.presentation.controller;

import com.coredisc.application.service.member.MemberQueryService;
import com.coredisc.application.service.searchHistory.SearchHistoryCommandService;
import com.coredisc.application.service.searchHistory.SearchHistoryQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.SearchControllerDocs;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import com.coredisc.presentation.dto.searchHistory.SearchHistoryResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerDocs {

    private final MemberQueryService memberQueryService;
    private final SearchHistoryQueryService searchHistoryQueryService;
    private final SearchHistoryCommandService searchHistoryCommandService;

    private static final int DEFAULT_PAGE_SIZE = 10; // 한페이지당 개수


    @GetMapping("/members")
    public ApiResponse<CursorDTO<MemberResponseDTO.SearchMemberResultDTO>> getMemberSearchList(
            @CurrentMember Member member,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "record") Boolean record,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size) {

        if (size == null)
            size = DEFAULT_PAGE_SIZE;

        return ApiResponse.onSuccess(memberQueryService.getMemberSearchList(member, keyword, record, cursorId, size));

    }

    @GetMapping("/members/history")
    public ApiResponse<CursorDTO<SearchHistoryResponseDTO.MySearchHistoryResultDTO>> getMemberSearchHistoryList(
            @CurrentMember Member member,
            @RequestParam(name = "cursorSearchedAt", required = false) LocalDateTime cursorSearchedAt,
            @RequestParam(name = "size", required = false) Integer size) {

        if (size == null)
            size = DEFAULT_PAGE_SIZE;

        return ApiResponse.onSuccess(searchHistoryQueryService.getMemberSearchHistoryList(member, cursorSearchedAt, size));
    }

    @DeleteMapping("/member/history/{historyId}")
    public ApiResponse<String> deleteSearchHistory(@CurrentMember Member member, @PathVariable(name = "historyId") Long historyId) {

        searchHistoryCommandService.deleteSearchHistory(member, historyId);

        return ApiResponse.onSuccess("검색 기록이 삭제되었습니다.");
    }
}
