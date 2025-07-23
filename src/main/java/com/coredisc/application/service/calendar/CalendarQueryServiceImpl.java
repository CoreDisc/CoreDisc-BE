package com.coredisc.application.service.calendar;

import com.coredisc.common.converter.CalendarConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.presentation.dto.calendar.CalendarPostDTO;
import com.coredisc.presentation.dto.calendar.CalendarResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarQueryServiceImpl implements CalendarQueryService {
    private final PostRepository postRepository;

    public CalendarResponseDTO.CalendarDTO getCalendar(int year, int month, Member member){
        LocalDate today = LocalDate.now();

        List<CalendarPostDTO> posts = postRepository.findPostInfoByMemberAndMonth(year, month, member);

        Map<Integer, Long> dayToPostIdMap = posts.stream()
                .collect(Collectors.toMap(
                        dto -> dto.getCreatedAt().getDayOfMonth(),
                        CalendarPostDTO::getPostId
                ));

        List<CalendarResponseDTO.DayResultDTO> days = CalendarConverter.toDayResultDTO(year, month, dayToPostIdMap);

        int totalDays = posts.size();

        int continuesDays = 0;
        if (year == today.getYear() && month == today.getMonthValue()) {
            int day = today.getDayOfMonth();
            while (day > 0 && dayToPostIdMap.containsKey(day)) {
                continuesDays++;
                day--;
            }
        }

        return CalendarConverter.toCalendarDTO(year, month, days, totalDays, continuesDays, LocalDate.from(member.getCreatedAt()));
    }
}