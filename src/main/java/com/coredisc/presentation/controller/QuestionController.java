package com.coredisc.presentation.controller;

import com.coredisc.application.service.question.QuestionCommandService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.QuestionConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.QuestionControllerDocs;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController implements QuestionControllerDocs {

    private final QuestionCommandService questionCommandService;

    // 내가 작성한 질문 저장
    @PostMapping("/personal")
    public ApiResponse<QuestionResponseDTO.savePersonalQuestionResultDTO> savePersonalQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SavePersonalQuestionDTO request) {

        return ApiResponse.onSuccess(QuestionConverter.toSavePersonalQuestionResultDTO(questionCommandService.savePersonalQuestion(request, member)));
    }
}
