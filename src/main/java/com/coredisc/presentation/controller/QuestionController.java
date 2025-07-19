package com.coredisc.presentation.controller;

import com.coredisc.application.service.question.QuestionCommandService;
import com.coredisc.application.service.question.QuestionQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.QuestionConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.QuestionControllerDocs;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController implements QuestionControllerDocs {

    private final QuestionCommandService questionCommandService;
    private final QuestionQueryService questionQueryService;
    private static final int DEFAULT_PAGE_SIZE = 15; // 한페이지당 질문 개수

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
    @GetMapping("/basic/categories/{categoryId}")
    public ApiResponse<QuestionResponseDTO.BasicQuestionListResultDTO> getBasicQuestionList(@CurrentMember Member member, @PathVariable(name = "categoryId") Long categoryId,  @RequestParam(name = "page") Integer page) {

        return ApiResponse.onSuccess(QuestionConverter.toBasicQuestionListResultDTO(questionQueryService.getBasicQuestionList(member, categoryId, PageRequest.of(page, DEFAULT_PAGE_SIZE))));
    }
    
    // 기본 질문 검색 리스트 조회
    @GetMapping("/basic/search")
    public ApiResponse<QuestionResponseDTO.BasicQuestionListResultDTO> getBasicQuestionSearchList(@CurrentMember Member member, @RequestParam(name = "keyword") String keyword, @RequestParam(name = "page") Integer page){

        return ApiResponse.onSuccess(QuestionConverter.toBasicQuestionListResultDTO(questionQueryService.getBasicQuestionSearchList(member, keyword, PageRequest.of(page, DEFAULT_PAGE_SIZE))));
    }

    // 내가 발행한 공유 질문 리스트 조회
    @GetMapping("/official/mine")
    public ApiResponse<QuestionResponseDTO.MySharedQuestionListResultDTO> getMySharedQuestionList(@CurrentMember Member member, @RequestParam(name = "page") Integer page){

        return ApiResponse.onSuccess(QuestionConverter.toMySharedQuestionListResultDTO(questionQueryService.getMySharedQuestionList(member, PageRequest.of(page, DEFAULT_PAGE_SIZE))));

    }
}
