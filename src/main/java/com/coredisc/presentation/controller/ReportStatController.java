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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
public class ReportStatController implements ReportStatControllerDocs {

    private final ReportStatQueryService reportStatQueryService;

    // 사용자의 월별 리포트 조회
    @GetMapping
    public ApiResponse<ReportStatResponseDTO.MonthlyReportDTO> getMonthlyReport(@RequestParam int year, @RequestParam int month, @CurrentMember Member member) {
        return ApiResponse.onSuccess(ReportStatConverter.toMonthlyReport(reportStatQueryService.getMonthlyReportRawData(year, month, member.getId())));
    }

    // 사용자의 선택형 일기 리포트 조회(1) - 가장 많이 선택한 옵션 조회
    @GetMapping("/daily/top-selection")
    public ApiResponse<ReportStatResponseDTO.TopDailySelectionDTO> getMostSelectedDaily(@RequestParam int year, @RequestParam int month, @CurrentMember Member member) {
        return ApiResponse.onSuccess(ReportStatConverter.toTopDailySelectionDTO(reportStatQueryService.getMostSelectedDaily(year, month, member.getId())));
    }

    // 사용자의 선택형 일기 리포트 조회(2) - 일기 내용 전체 출력
    @GetMapping("/daily/details")
    public ApiResponse<ReportStatResponseDTO.DailyDetailListDTO> getDailyDetail(@RequestParam int year, @RequestParam int month, @CurrentMember Member member) {
        return ApiResponse.onSuccess(ReportStatConverter.toDailyDetailListDTO(reportStatQueryService.getDailyDetails(year, month, member)));
    }
}