package com.coredisc.presentation.controller;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.controllerdocs.ReportStatControllerDocs;
import com.coredisc.presentation.dto.report.ReportResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportStatController implements ReportStatControllerDocs {

    // 사용자가 특정 기간에 선택한 모든 질문 목록 조회
    @GetMapping("/question-list")
    public ApiResponse<ReportResponseDTO.QuestionListDTO> getMonthlyQuestionList(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("memberId") Long memberId) {

        return ApiResponse.onSuccess(null);
    }

    // 최다 선택된 랜덤 질문 조회
    @GetMapping("/most-selected")
    public ApiResponse<ReportResponseDTO.MostSelectedQuestionDTO> getMostSelectedQuestions(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("memberId") Long memberId) {

        return ApiResponse.onSuccess(null);
    }

    // 최다 답변 시간대 조회
    @GetMapping("/peak-hours")
    public ApiResponse<ReportResponseDTO.PeakHourDTO> getPeakHour(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("memberId") Long memberId) {

        return ApiResponse.onSuccess(null);
    }

    // 선택형 일기 최다 선택 옵션 조회
    @GetMapping("/daily")
    public ApiResponse<ReportResponseDTO.TopDailySelectionDTO> getMostSelectedDaily(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("memberId") Long memberId) {

        return ApiResponse.onSuccess(null);
    }
}