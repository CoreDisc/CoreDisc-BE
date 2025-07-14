package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.report.ReportResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Tag(name = "리포트 세부 통계", description = "사용자별 세부 통계 API")
public interface ReportStatControllerDocs {

    @Operation(summary = "기간별 전체 질문 목록 조회", description = "사용자가 특정 달에 선택한 고정 질문 3개와 랜덤 질문 목록을 조회합니다.")
    ApiResponse<ReportResponseDTO.QuestionListDTO> getMonthlyQuestionList(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @Parameter(hidden = true) @CurrentMember Member member
    );

    @Operation(summary = "기간별 최다 선택된 랜덤 질문 3순위 조회", description = "사용자가 특정 달 동안 가장 많이 선택한 랜덤 질문 3개를 조회합니다.")
    ApiResponse<ReportResponseDTO.MostSelectedQuestionDTO> getMostSelectedQuestions(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(hidden = true) @CurrentMember Member member
    );

    @Operation(summary = "기간별 응답 시간대 횟수 조회", description = "사용자의 특정 월(startDate~endDate)에 가장 많이 답변한 시간대와 시간대별 응답 수 리스트를 조회합니다.")
    ApiResponse<ReportResponseDTO.PeakHourDTO> getPeakHour(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(hidden = true) @CurrentMember Member member
    );

    @Operation(summary = "기간별 게시글의 daily_ 항목 최다 답변 조회", description = "사용자가 선택형 일기에서 특정 달에 가장 많이 선택한 옵션을 조회합니다.")
    ApiResponse<ReportResponseDTO.TopDailySelectionDTO> getMostSelectedDaily(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @Parameter(hidden = true) @CurrentMember Member member
    );

    @Operation(summary = "사용자가 특정 달에 작성한 일기 내용 전체 출력", description = "특정 기간 동안 일기 게시글의 daily_ 항목 중 가장 많이 선택된 옵션을 조회합니다.")
    ApiResponse<ReportResponseDTO.DailyDetailListDTO> getDailyDetail(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @Parameter(hidden = true) @CurrentMember Member member
    );
}