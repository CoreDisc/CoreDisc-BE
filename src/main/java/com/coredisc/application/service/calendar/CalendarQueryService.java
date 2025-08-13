package com.coredisc.application.service.calendar;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.calendar.CalendarResponseDTO;

public interface CalendarQueryService {

    CalendarResponseDTO.CalendarDTO getCalendar(int year, int month, Member member);

    Integer getContinuousDays(Member member);
}
