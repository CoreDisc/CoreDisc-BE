package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(name = "Question", description = "질문 관련 API")
public interface CategoryControllerDocs {

    @Operation(summary = "질문 카테고리 리스트 조회", description = "질문 카테고리 리스트 조회하는 기능입니다.")
    ApiResponse<List<CategoryResponseDTO.CategoryDTO>> getCategoryList();

}
