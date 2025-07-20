package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.block.BlockResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Block", description = "차단 관련 API")
public interface BlockControllerDocs {

    @Operation(summary = "차단", description = "차단 기능입니다.")
    ApiResponse<BlockResponseDTO.BlockResultDTO> block(@CurrentMember Member member, @PathVariable Long targetId);

    @Operation(summary = "차단 취소", description = "차단 취소 기능입니다.")
    ApiResponse<String> unblock(@CurrentMember Member member, @PathVariable Long targetId);

    @Operation(summary = "차단한 유저 목록 조회", description = "차단한 유저 목록 조회 기능입니다. 커서 기반 페이징입니다.")
    @Parameters({
            @Parameter(name = "cursorId", description = "마지막으로 조회한 blockId입니다. 첫 요청 때는 null, queryString입니다."),
            @Parameter(name = "size", description = "기본값 10")
    })
    ApiResponse<CursorDTO<BlockResponseDTO.BlockedDTO>> getBlockedList(@CurrentMember Member member,
                                                                       @RequestParam(required = false) Long cursorId,
                                                                       @RequestParam(required = false) Integer size);
}
