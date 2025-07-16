package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.dto.terms.TermsResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Terms", description = "이용약관 도메인 API")
public interface TermsControllerDocs {

    @Operation(summary = "이용 약관 리스트 조회", description = "회원가입을 위한 이용 약관 리스트 조회 기능입니다.")
    ApiResponse<List<TermsResponseDTO.TermsDTO>> getTermsList();
}
