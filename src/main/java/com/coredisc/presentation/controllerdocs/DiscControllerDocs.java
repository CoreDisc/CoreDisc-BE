package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.disc.DiscRequestDTO;
import com.coredisc.presentation.dto.disc.DiscResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface DiscControllerDocs {

    @Operation(summary = "디스크 목록 조회", description = "사용자의 월간 디스크 목록을 조회합니다.")
    ApiResponse<DiscResponseDTO.DiscListDTO> getDiscList(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam(name = "page") int page,
            @Parameter(hidden = true) @CurrentMember Member member
    );

    @Operation(summary = "디스크 조회", description = "디스크를 조회합니다.")
    ApiResponse<DiscResponseDTO.DiscDTO> getDisc(
            @Parameter(description = "디스크 ID", example = "1")
            @PathVariable(name = "discId") Long discId,
            @Parameter(hidden = true) @CurrentMember Member member
    );

    @Operation(summary = "디스크 커버 이미지 변경", description = "디스크 커버 이미지를 변경합니다.")
    ApiResponse<DiscResponseDTO.DiscDTO> updateDiscCoverImage(
            @Parameter(description = "디스크 ID", example = "1") @PathVariable(name = "discId") Long discId,
            @RequestBody(description = "새로운 커버 이미지 URL 정보") DiscRequestDTO.UpdateCoverImgDTO request,
            @Parameter(hidden = true) @CurrentMember Member member
    );

    @Operation(summary = "디스크 커버 색깔 변경", description = "디스크 커버 색깔을 변경합니다.")
    ApiResponse<DiscResponseDTO.DiscDTO> updateDiscCoverColor(
            @Parameter(description = "디스크 ID", example = "1") @PathVariable(name = "discId") Long discId,
            @RequestBody(description = "새로운 커버 색상 정보") DiscRequestDTO.UpdateCoverColorDTO request,
            @Parameter(hidden = true) @CurrentMember Member member
    );
}
