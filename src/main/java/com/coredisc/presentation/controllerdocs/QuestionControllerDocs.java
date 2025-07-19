package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Question", description = "질문 관련 API")
public interface QuestionControllerDocs {

    @Operation(summary = "내가 작성한 질문 저장하기", description = "내가 커스텀한 질문을 저장하는 기능입니다.")
    ApiResponse<QuestionResponseDTO.savePersonalQuestionResultDTO> savePersonalQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SavePersonalQuestionDTO request);

    @Operation(summary = "내가 작성한 질문 공유하기", description = "내가 커스텀한 질문을 공유하는 기능입니다.")
    ApiResponse<QuestionResponseDTO.saveOfficialQuestionResultDTO> saveOfficialQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SaveOfficialQuestionDTO request);

    @Operation(summary = "기본 질문 리스트 조회 (카테고리별)", description = "카테고리별로 기본 질문 리스트를 조회하는 기능입니다.")
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리ID pathVariable입니다.", in = ParameterIn.PATH),
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)"),
    })
    ApiResponse<QuestionResponseDTO.BasicQuestionListResultDTO> getBasicQuestionList(@CurrentMember Member member, @PathVariable(name = "categoryId") Long categoryId, @RequestParam(name = "page") Integer page);

    @Operation(summary = "기본 질문 리스트 검색 조회", description = "기본 질문을 검색하는 기능입니다. (키워드가 카테고리명과 일치 시 해당 카테고리에 속하는 기본 질문들도 포함)")
    @Parameters({
            @Parameter(name = "keyword", description = "검색어입니다."),
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)"),
    })
    ApiResponse<QuestionResponseDTO.BasicQuestionListResultDTO> getBasicQuestionSearchList(@CurrentMember Member member, @RequestParam(name = "keyword") String keyword, @RequestParam(name = "page") Integer page);

    @Operation(summary = "내가 발행한 공유질문 리스트 조회 (카테고리 필터링 가능)", description = "사용자 본인이 발행한 공유질문 리스트를 조회하는 기능입니다. (카테고리 필터링 가능)")
    @Parameters({
            @Parameter(name = "category", description = "카테고리ID입니다. (0 또는 생략 시 전체 조회)"),
            @Parameter(name = "page", description = "페이지 번호 (0부터 시작)"),
    })
    ApiResponse<QuestionResponseDTO.MySharedQuestionListResultDTO> getMySharedQuestionList(@CurrentMember Member member, @RequestParam(name = "category", required = false) Long category, @RequestParam(name = "page") Integer page);

}
