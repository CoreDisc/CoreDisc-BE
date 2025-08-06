package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Question", description = "질문 관련 API")
public interface CategoryControllerDocs {

    @Operation(summary = "질문 카테고리 리스트 조회", description = "질문 카테고리 리스트 조회하는 기능입니다.")
    ApiResponse<List<CategoryResponseDTO.CategoryDTO>> getCategoryList(@CurrentMember Member member);


    @Operation(summary = "질문 카테고리 리스트 조회 (검색 ver)", description = "질문 카테고리 리스트와 검색어에 해당되는 카테고리별 질문 개수를 조회하는 기능입니다.")
    @Parameters({
            @Parameter(name = "keyword", description = "검색어입니다.")
    })
    ApiResponse<List<CategoryResponseDTO.CategoryDTO>> getCategoryListByKeyword(@CurrentMember Member member, @RequestParam(name = "keyword") String keyword);

}
