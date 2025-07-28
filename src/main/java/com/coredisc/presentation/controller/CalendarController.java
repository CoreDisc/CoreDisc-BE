package com.coredisc.presentation.controller;

import com.coredisc.application.service.calendar.CalendarQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.controllerdocs.CalendarControllerDocs;
import com.coredisc.presentation.dto.calendar.CalendarResponseDTO;
import com.coredisc.security.jwt.annotaion.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports/calendar")
public class CalendarController implements CalendarControllerDocs {

    private final CalendarQueryService calendarQueryService;

    // 캘린더 조회
    @GetMapping
    public ApiResponse<CalendarResponseDTO.CalendarDTO> getCalendar(@RequestParam int year, @RequestParam int month, @CurrentMember Member member) {
        return ApiResponse.onSuccess(calendarQueryService.getCalendar(year, month, member));
    }
}
