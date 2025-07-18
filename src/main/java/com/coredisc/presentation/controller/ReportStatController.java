package com.coredisc.presentation.controller;

import com.coredisc.application.service.reportStat.ReportStatQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.ReportStatConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.ReportStatControllerDocs;
import com.coredisc.presentation.dto.reportStat.ReportStatResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportStatController implements ReportStatControllerDocs {

    private final ReportStatQueryService reportStatQueryService;

    // 사용자가 특정 달에 선택한 고정 질문 3개와 랜덤 질문 목록 조회
    @GetMapping("/question-list")
    public ApiResponse<ReportStatResponseDTO.QuestionListDTO> getMonthlyQuestionList(int year, int month, @CurrentMember Member member) {
        return ApiResponse.onSuccess(ReportStatConverter.toQuestionListDTO(reportStatQueryService.getQuestionList(year, month, member.getId())));
    }

    // 사용자가 특정 달 동안 가장 많이 선택한 랜덤 질문 3개 조회
    @GetMapping("/most-selected")
    public ApiResponse<ReportStatResponseDTO.MostSelectedQuestionDTO> getMostSelectedQuestions(int year, int month, @CurrentMember Member member) {
        return ApiResponse.onSuccess(ReportStatConverter.toMostSelectedQuestionDTO(reportStatQueryService.getMostSelectedQuestions(year, month, member.getId())));
    }

    // 사용자가 특정 달에 시간대 별로 응답한 횟수 조회
    @GetMapping("/peak-hours")
    public ApiResponse<ReportStatResponseDTO.PeakHourDTO> getPeakHour(int year, int month, @CurrentMember Member member) {
        return ApiResponse.onSuccess(ReportStatConverter.toPeakHourDTO(reportStatQueryService.getHourlyAnswerCountMap(year, month, member.getId())));
    }

    // 사용자가 선택형 일기에서 특정 달에 가장 많이 선택한 옵션 조회
    @GetMapping("/daily/top-selection")
    public ApiResponse<ReportStatResponseDTO.TopDailySelectionDTO> getMostSelectedDaily(int year, int month, @CurrentMember Member member) {
        return ApiResponse.onSuccess(ReportStatConverter.toTopDailySelectionDTO(reportStatQueryService.getMostSelectedDaily(year, month, member.getId())));
    }

    // 사용자가 특정 달에 작성한 일기 내용 전체 출력
    // TODO : 추후 작성할 예정
    @GetMapping("/daily/details")
    public ApiResponse<ReportStatResponseDTO.DailyDetailListDTO> getDailyDetail(int year, int month, @CurrentMember Member member) {
        return ApiResponse.onSuccess(null);
    }
}