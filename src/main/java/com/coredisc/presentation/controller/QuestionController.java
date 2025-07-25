package com.coredisc.presentation.controller;

import com.coredisc.application.service.question.QuestionCommandService;
import com.coredisc.application.service.question.QuestionQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.QuestionConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.QuestionControllerDocs;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController implements QuestionControllerDocs {

    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;
    private static final int DEFAULT_PAGE_SIZE = 10; // 한페이지당 질문 개수

    // 내가 작성한 질문 저장
    @PostMapping("/personal")
    public ApiResponse<QuestionResponseDTO.savePersonalQuestionResultDTO> savePersonalQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SavePersonalQuestionDTO request) {

        return ApiResponse.onSuccess(QuestionConverter.toSavePersonalQuestionResultDTO(questionCommandService.savePersonalQuestion(request, member)));
    }

    // 내가 작성한 질문 공유
    @PostMapping("/official")
    public ApiResponse<QuestionResponseDTO.saveOfficialQuestionResultDTO> saveOfficialQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SaveOfficialQuestionDTO request) {

        return ApiResponse.onSuccess(QuestionConverter.toSaveOfficialQuestionResultDTO(questionCommandService.saveOfficialQuestion(request, member)));
    }

    // 기본 질문 리스트 조회 (카테고리별)
    @GetMapping("/basic")
    public ApiResponse<CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO>> getBasicQuestionList(
            @CurrentMember Member member,
            @RequestParam(name = "categoryId") Long categoryId,
            @RequestParam(name = "cursorCreatedAt", required = false) String cursorCreatedAtStr,
            @RequestParam(name = "cursorQuestionType", required = false) String cursorQuestionType,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size
    ) {

        LocalDateTime cursorCreatedAt = null;
        if (cursorCreatedAtStr != null && !cursorCreatedAtStr.isEmpty()) {
            cursorCreatedAt = LocalDateTime.parse(cursorCreatedAtStr);
        }

        if (size == null)
            size = DEFAULT_PAGE_SIZE;

        return ApiResponse.onSuccess( questionQueryService.getBasicQuestionList(member, categoryId, cursorCreatedAt, cursorQuestionType, cursorId, size) );
    }

    
    // 기본 질문 검색 리스트 조회
    @GetMapping("/basic/search")
    public ApiResponse<CursorDTO<QuestionResponseDTO.BasicQuestionResultDTO>> getBasicQuestionSearchList(
            @CurrentMember Member member,
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "cursorCreatedAt", required = false) String cursorCreatedAtStr,
            @RequestParam(name = "cursorQuestionType", required = false) String cursorQuestionType,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size) {

        LocalDateTime cursorCreatedAt = null;
        if (cursorCreatedAtStr != null && !cursorCreatedAtStr.isEmpty()) {
            cursorCreatedAt = LocalDateTime.parse(cursorCreatedAtStr);
        }

        if (size == null)
            size = DEFAULT_PAGE_SIZE;

        return ApiResponse.onSuccess( questionQueryService.getBasicQuestionSearchList(member, keyword, cursorCreatedAt, cursorQuestionType, cursorId, size) );
    }

    // 내가 발행한 공유 질문 리스트 조회 (카테고리 구분 포함)
    @GetMapping("/official/mine")
    public ApiResponse<QuestionResponseDTO.MySharedQuestionListResultDTO> getMySharedQuestionList(
            @CurrentMember Member member,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            @RequestParam(name = "cursorId", required = false) Long cursorId,
            @RequestParam(name = "size", required = false) Integer size) {

        if (size == null)
            size = DEFAULT_PAGE_SIZE;

        return ApiResponse.onSuccess(questionQueryService.getMySharedQuestionList(member, categoryId, cursorId, size));

    }

    // 고정 질문 선택
    @PostMapping("/fixed")
    public ApiResponse<QuestionResponseDTO.SaveFixedTodayQuestionResultDTO> saveFixedTodayQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SaveFixedTodayQuestionDTO request){

        return ApiResponse.onSuccess(QuestionConverter.toSaveFixedTodayQuestionResultDTO(questionCommandService.saveFixedTodayQuestion(request, member)));
    }

    // 랜덤 질문 선택
    @PostMapping("/random")
    public ApiResponse<QuestionResponseDTO.SaveRandomTodayQuestionResultDTO> saveRandomTodayQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SaveRandomTodayQuestionDTO request) {

        return ApiResponse.onSuccess(QuestionConverter.toSaveRandomTodayQuestionResultDTO(questionCommandService.saveRandomTodayQuestion(request, member)));
    }

    // 선택한 고정&랜덤 질문 조회
    @GetMapping("/selected")
    public ApiResponse<List<QuestionResponseDTO.SelectedTodayQuestionResultDTO>> getMyTodayQuestion(@CurrentMember Member member) {

        return ApiResponse.onSuccess(questionQueryService.getMyTodayQuestion(member));
    }

    // 사용자가 작성하여 저장했던 질문 수정
    public ApiResponse<QuestionResponseDTO.savePersonalQuestionResultDTO> updatePersonalQuestion(@CurrentMember Member member, @PathVariable(name = "questionId") Long questionId, @Valid @RequestBody QuestionRequestDTO.SavePersonalQuestionDTO request) {

        return ApiResponse.onSuccess(QuestionConverter.toSavePersonalQuestionResultDTO(questionCommandService.updatePersonalQuestion(member, questionId, request)));
    }

    // 사용자가 작성하여 저장했던 질문 삭제
    @DeleteMapping("/personal/{questionId}")
    public ApiResponse<String> deletePersonalQuestion(@CurrentMember Member member, @PathVariable(name = "questionId") Long questionId) {

        questionCommandService.deletePersonalQuestion(member, questionId);

        return ApiResponse.onSuccess("질문이 삭제되었습니다.");
    }

    // 타사용자가 작성한 공유 질문 저장
    @PostMapping("/official/{questionId}")
    public ApiResponse<QuestionResponseDTO.SaveMemberOfficialQuestionResultDTO> saveMemberOfficialQuestion(@CurrentMember Member member, @PathVariable(name = "questionId") Long questionId) {

        return ApiResponse.onSuccess(QuestionConverter.toSaveMemberOfficialQuestionResultDTO(questionCommandService.saveMemberOfficialQuestion(member, questionId)));
    }

    // 저장헀던 공유 질문을 삭제
    @DeleteMapping("/official/saved/{questionId}")
    public ApiResponse<String> deleteMemberOfficialQuestion(@CurrentMember Member member, @PathVariable(name = "questionId") Long questionId) {

        questionCommandService.deleteMemberOfficialQuestion(member, questionId);

        return ApiResponse.onSuccess("질문이 삭제되었습니다.");
    }

}
