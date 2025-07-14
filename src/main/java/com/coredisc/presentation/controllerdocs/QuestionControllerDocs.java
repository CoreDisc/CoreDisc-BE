package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.question.QuestionRequestDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Question", description = "질문 관련 API")
public interface QuestionControllerDocs {

    @Operation(summary = "내가 작성한 질문 저장하기", description = "내가 커스텀한 질문을 저장하는 기능입니다.")
    public ApiResponse<QuestionResponseDTO.savePersonalQuestionResultDTO> savePersonalQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SavePersonalQuestionDTO request);

    @Operation(summary = "내가 작성한 질문 공유하기", description = "내가 커스텀한 질문을 공유하는 기능입니다.")
    public ApiResponse<QuestionResponseDTO.saveOfficialQuestionResultDTO> saveOfficialQuestion(@CurrentMember Member member, @Valid @RequestBody QuestionRequestDTO.SaveOfficialQuestionDTO request);

}
