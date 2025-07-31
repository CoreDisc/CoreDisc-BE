package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.reportStat.ReportStatResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Monthly Report", description = "사용자의 월별 리포트 API")
public interface ReportStatControllerDocs {

    @Operation(summary = "사용자의 월별 리포트 조회", description = "특정 기간 동안 사용자의 활동에 대한 월별 리포트를 조회합니다.")
    ApiResponse<ReportStatResponseDTO.MonthlyReportDTO> getMonthlyReport(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @Parameter(hidden = true) @CurrentMember Member member
    );

    @Operation(summary = "사용자가 특정 달에 작성한 일기 내용 전체 출력", description = "특정 기간 동안 일기 게시글의 daily_ 항목 중 가장 많이 선택된 옵션을 조회합니다.")
    ApiResponse<ReportStatResponseDTO.DailyDetailListDTO> getDailyDetail(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @Parameter(hidden = true) @CurrentMember Member member
    );

    @Operation(summary = "기간별 게시글의 daily_ 항목 최다 답변 조회", description = "사용자가 선택형 일기에서 특정 달에 가장 많이 선택한 옵션을 조회합니다.")
    ApiResponse<ReportStatResponseDTO.TopDailySelectionDTO> getMostSelectedDaily(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @Parameter(hidden = true) @CurrentMember Member member
    );


}