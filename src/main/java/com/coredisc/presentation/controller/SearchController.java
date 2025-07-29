package com.coredisc.presentation.controller;

import com.coredisc.application.service.member.MemberQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.SearchControllerDocs;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController implements SearchControllerDocs {

    private final MemberQueryService memberQueryService;
    private static final int DEFAULT_PAGE_SIZE = 10; // 한페이지당 개수


    @GetMapping("/members")
    public ApiResponse<CursorDTO<MemberResponseDTO.SearchMemberResultDTO>> getMemberSearchList(
            @CurrentMember Member member,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size) {

        if (size == null)
            size = DEFAULT_PAGE_SIZE;

        return ApiResponse.onSuccess(memberQueryService.getMemberSearchList(member, keyword, cursorId, size));

    }
}
