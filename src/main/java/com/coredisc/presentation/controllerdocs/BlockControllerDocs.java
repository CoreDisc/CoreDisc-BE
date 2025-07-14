package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.block.BlockResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Block", description = "차단 관련 API")
public interface BlockControllerDocs {

    @Operation(summary = "차단", description = "차단 기능입니다.")
    ApiResponse<BlockResponseDTO.BlockResultDTO> block(@CurrentMember Member member, @PathVariable Long targetId);

    @Operation(summary = "차단 취소", description = "차단 취소 기능입니다.")
    ApiResponse<String> unblock(@CurrentMember Member member, @PathVariable Long targetId);
}
