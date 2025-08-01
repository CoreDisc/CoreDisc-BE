package com.coredisc.presentation.controllerdocs;


import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Search", description = "검색 관련 API")
public interface SearchControllerDocs {

    @Operation(summary = "검색 화면 검색한 사용자 목록 조회", description = "검색한 사용자 목록을 조회하는 기능입니다.")
    @Parameters({
            @Parameter(name = "keyword", description = "검색어"),
            @Parameter(name = "record", description = "최근 검색 기록에 저장할지 여부"),
            @Parameter(name = "cursorId", description = "커서 - 마지막 사용자 ID, 첫 요청 때는 null"),
            @Parameter(name = "size", description = "한 페이지당 조회할 질문 수, 기본값 10"),
    })
    ApiResponse<CursorDTO<MemberResponseDTO.SearchMemberResultDTO>> getMemberSearchList(
            @CurrentMember Member member,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "record", required = false) Boolean record,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size);
}
