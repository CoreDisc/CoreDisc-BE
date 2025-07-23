package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.calendar.CalendarResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Calendar", description = "캘린더 관련 API")
public interface CalendarControllerDocs {

    @Operation(summary = "월간 답변 기록 캘린더 조회", description = "사용자의 한 달 간의 답변 작성 여부와 연속 답변 일수를 조회합니다.")
    ApiResponse<CalendarResponseDTO.CalendarDTO> getCalendar(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            @Parameter(hidden = true) @CurrentMember Member member
    );
}