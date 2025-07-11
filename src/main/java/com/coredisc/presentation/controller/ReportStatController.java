package com.coredisc.presentation.controller;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.controllerdocs.ReportStatControllerDocs;
import com.coredisc.presentation.dto.report.ReportResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportStatController implements ReportStatControllerDocs {

    // 사용자가 특정 달에 선택한 고정 질문 3개와 랜덤 질문 목록 조회
    @GetMapping("/question-list")
    public ApiResponse<ReportResponseDTO.QuestionListDTO> getMonthlyQuestionList(int year, int month, Long memberId) {
        return ApiResponse.onSuccess(null);
    }

    // 사용자가 특정 달 동안 가장 많이 선택한 랜덤 질문 3개 조회
    @GetMapping("/most-selected")
    public ApiResponse<ReportResponseDTO.MostSelectedQuestionDTO> getMostSelectedQuestions(LocalDate startDate, LocalDate endDate, Long memberId) {
        return ApiResponse.onSuccess(null);
    }

    // 사용자가 특정 달에 시간대 별로 응답한 횟수 조회
    @GetMapping("/peak-hours")
    public ApiResponse<ReportResponseDTO.PeakHourDTO> getPeakHour(LocalDate startDate, LocalDate endDate, Long memberId) {
        return ApiResponse.onSuccess(null);
    }

    // 사용자가 선택형 일기에서 특정 달에 가장 많이 선택한 옵션 조회
    @GetMapping("/daily")
    public ApiResponse<ReportResponseDTO.TopDailySelectionDTO> getMostSelectedDaily(int year, int month, Long memberId) {
        return ApiResponse.onSuccess(null);
    }

    // 사용자가 특정 달에 작성한 일기 내용 전체 출력
    public ApiResponse<ReportResponseDTO.DailyDetailListDTO> getDailyDetail(int year, int month, Long memberId) {
        return ApiResponse.onSuccess(null);
    }
}