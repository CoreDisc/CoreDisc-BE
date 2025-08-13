package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Question", description = "질문 관련 API")
public interface QuestionControllerDocs {

    @Operation(summary = "내가 작성한 질문 저장하기", description = "내가 커스텀한 질문을 저장하는 기능입니다.")
    ApiResponse<QuestionResponseDTO.savePersonalQuestionResultDTO> savePersonalQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SavePersonalQuestionDTO request);

    @Operation(summary = "내가 작성한 질문 공유하기", description = "내가 커스텀한 질문을 공유하는 기능입니다.")
    ApiResponse<QuestionResponseDTO.saveOfficialQuestionResultDTO> saveOfficialQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SaveOfficialQuestionDTO request);

    @Operation(summary = "기본 질문 리스트 조회 (카테고리별)", description = "카테고리별로 기본 질문 리스트를 조회하는 기능입니다.")
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리ID", required = true),
            @Parameter(name = "cursorCreatedAt", description = "커서 - 마지막 질문 생성일자 (ISO 8601 형식), 첫 요청 때는 null", required = false),
            @Parameter(name = "cursorQuestionType", description = "커서 - 마지막 질문 타입 (PERSONAL, OFFICIAL, DEFAULT), 첫 요청 때는 null", required = false),
            @Parameter(name = "cursorId", description = "커서 - 마지막 질문 ID, 첫 요청 때는 null", required = false),
            @Parameter(name = "size", description = "한 페이지당 조회할 질문 수, 기본값 10", required = false)
    })
    ApiResponse<CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO>> getBasicQuestionList(
            @CurrentMember Member member,
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "cursorCreatedAt", required = false) String cursorCreatedAt,
            @RequestParam(name = "cursorQuestionType", required = false) String cursorQuestionType,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size
    );

    @Operation(summary = "기본 질문 리스트 검색 조회 (카테고리별)", description = "기본 질문을 검색 후 카테고리별 조회 기능입니다. (키워드가 카테고리명과 일치 시 해당 카테고리에 속하는 기본 질문들도 포함)")
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리ID."),
            @Parameter(name = "keyword", description = "검색어입니다."),
            @Parameter(name = "cursorCreatedAt", description = "커서 - 마지막 질문 생성일자 (ISO 8601 형식), 첫 요청 때는 null", required = false),
            @Parameter(name = "cursorQuestionType", description = "커서 - 마지막 질문 타입 (PERSONAL, OFFICIAL, DEFAULT), 첫 요청 때는 null", required = false),
            @Parameter(name = "cursorId", description = "커서 - 마지막 질문 ID, 첫 요청 때는 null", required = false),
            @Parameter(name = "size", description = "한 페이지당 조회할 질문 수, 기본값 10", required = false)
    })
    ApiResponse<CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO>> getBasicQuestionSearchList(
            @CurrentMember Member member,
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "cursorCreatedAt", required = false) String cursorCreatedAt,
            @RequestParam(name = "cursorQuestionType", required = false) String cursorQuestionType,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size);

    @Operation(summary = "내가 현재 발행한 공유질문 리스트 조회 (발행 개수 포함 ver)", description = "사용자 현재 본인이 발행한 공유질문 리스트를 조회하는 기능입니다. (발행 개수 포함 ver)")
    @Parameters({
            @Parameter(name = "cursorId", description = "커서 - 마지막 질문 ID, 첫 요청 때는 null"),
            @Parameter(name = "size", description = "한 페이지당 조회할 질문 수, 기본값 10", required = false),
    })
    ApiResponse<QuestionResponseDTO.MySharedQuestionPreviewListResultDTO> getMySharedQuestionListPreview(
            @CurrentMember Member member,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size);

    @Operation(summary = "내가 발행한 공유질문 리스트 조회 (카테고리 필터링 ver)", description = "사용자 본인이 발행한 공유질문 리스트를 조회하는 기능입니다. (카테고리 필터링 ver)")
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리ID입니다. (0 또는 생략 시 전체 조회)"),
            @Parameter(name = "cursorId", description = "커서 - 마지막 질문 ID, 첫 요청 때는 null"),
            @Parameter(name = "size", description = "한 페이지당 조회할 질문 수, 기본값 10", required = false),
    })
    ApiResponse<CursorDTO<QuestionResponseDTO.MySharedQuestionResultDTO>> getMySharedQuestionList(
            @CurrentMember Member member,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size);

    @Operation(summary = "고정 질문 선택", description = "고정 질문을 선택하는 기능입니다.")
    ApiResponse<QuestionResponseDTO.SaveFixedTodayQuestionResultDTO> saveFixedTodayQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SaveFixedTodayQuestionDTO request);

    @Operation(summary = "랜덤 질문 선택", description = "랜덤 질문을 선택하는 기능입니다.")
    ApiResponse<QuestionResponseDTO.SaveRandomTodayQuestionResultDTO> saveRandomTodayQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SaveRandomTodayQuestionDTO request);

    @Operation(summary = "선택한 고정&랜덤 질문 조회", description = "선택한 고정 질문 3개와 랜덤 질문 1개 조회하는 기능입니다.")
    ApiResponse<List<QuestionResponseDTO.SelectedTodayQuestionResultDTO>> getMyTodayQuestion(@CurrentMember Member member);

    @Operation(summary = "커스텀 질문 수정", description = "사용자가 작성하여 저장했던 질문을 수정하는 기능입니다.")
    @Parameters({
            @Parameter(name = "questionId", description = "질문ID pathVariable입니다."),
    })
    ApiResponse<QuestionResponseDTO.savePersonalQuestionResultDTO> updatePersonalQuestion(@CurrentMember Member member, @PathVariable(name = "questionId") Long questionId, @Valid @RequestBody QuestionRequestDTO.SavePersonalQuestionDTO request);

    @Operation(summary = "커스텀 질문 삭제", description = "사용자가 작성하여 저장했던 질문을 삭제하는 기능입니다.")
    @Parameters({
            @Parameter(name = "questionId", description = "질문ID pathVariable입니다."),
    })
    ApiResponse<String> deletePersonalQuestion(@CurrentMember Member member, @PathVariable(name = "questionId") Long questionId);

    @Operation(summary = "타사용자가 작성한 공유 질문 저장", description = "타사용자가 발행한 공유 질문을 저장하는 기능입니다.")
    @Parameters({
            @Parameter(name = "questionId", description = "질문ID pathVariable입니다."),
    })
    ApiResponse<QuestionResponseDTO.SaveMemberOfficialQuestionResultDTO> saveMemberOfficialQuestion(@CurrentMember Member member, @PathVariable(name = "questionId") Long questionId);

    @Operation(summary = "저장했던 공유 질문 삭제", description = "타사용자가 발행하여 저장헀던 공유 질문을 삭제하는 기능입니다.")
    @Parameters({
            @Parameter(name = "questionId", description = "질문ID pathVariable입니다."),
    })
    ApiResponse<String> deleteMemberOfficialQuestion(@CurrentMember Member member, @PathVariable(name = "questionId") Long questionId);

    @Operation(summary = "내가 저장한 공유질문 리스트 조회", description = "사용자가 저장한 타사용자의 공유질문 리스트를 조회하는 기능입니다.")
    @Parameters({
            @Parameter(name = "categoryId", description = "카테고리ID입니다. (0 또는 생략 시 전체 조회)"),
            @Parameter(name = "cursorId", description = "커서 - 마지막 질문 ID, 첫 요청 때는 null"),
            @Parameter(name = "size", description = "한 페이지당 조회할 질문 수, 기본값 10"),
    })
    ApiResponse<CursorDTO<QuestionResponseDTO.SavedSharedQuestionResultDTO>> getSavedSharedQuestionList(
            @CurrentMember Member member,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size);

    @Operation(summary = "인기 질문 목록 조회", description = "인기 질문 상위 5개를 조회하는 기능입니다.")
    ApiResponse<QuestionResponseDTO.PopularQuestionListResultDTO> getPopularQuestionList(@CurrentMember Member member);
}
