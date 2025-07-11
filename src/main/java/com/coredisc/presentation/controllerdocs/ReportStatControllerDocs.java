package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.report.ReportResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "리포트 세부 통계", description = "사용자별 세부 통계 API")
public interface ReportStatControllerDocs {

    @Operation(summary = "월별 전체 질문 목록 조회", description = "사용자가 한 달동안 답변한 고정 질문 3개와 랜덤 질문 목록을 조회합니다.")
    ApiResponse<ReportResponseDTO.QuestionListDTO> getMonthlyQuestionList(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            Member member
    );

    @Operation(summary = "최다 선택된 랜덤 질문 조회", description = "한 달 동안 사용자가 가장 많이 선택한 랜덤 질문 3가지를 조회합니다.")
    ApiResponse<ReportResponseDTO.MostSelectedQuestionDTO> getMostSelectedQuestions(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            Member member
    );

    @Operation(summary = "최다 답변 시간대 조회", description = "한 달 동안 사용자가 가장 많이 답변한 시간대를 조회합니다.")
    ApiResponse<ReportResponseDTO.PeakHourDTO> getPeakHour(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            Member member
    );

    @Operation(summary = "게시글의 daily_ 항목 최다 답변 조회", description = "일기 게시글의 daily_ 항목 중 가장 많이 선택된 옵션을 조회합니다.")
    ApiResponse<ReportResponseDTO.TopDailySelectionDTO> getMostSelectedDaily(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            Member member
    );
}