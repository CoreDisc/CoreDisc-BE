package com.coredisc.application.service.calendar;

import com.coredisc.common.converter.CalendarConverter;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.presentation.dto.calendar.CalendarPostDTO;
import com.coredisc.presentation.dto.calendar.CalendarResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

        return CalendarConverter.toCalendarDTO(year, month, days, totalDays, LocalDate.from(member.getCreatedAt()));
    }

    @Override
    public Integer getContinuousDays(Member member) {
        LocalDate today = LocalDate.now();
        int continuousDays = 0;
        int batchSize = 100; // 100일 단위로 점진적 조회
        
        // 첫 번째 조회: 오늘부터 100일 전까지
        LocalDate startDate = today.minusDays(batchSize - 1);
        List<Post> posts = postRepository.findAllByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(
            member, 
            startDate.atStartOfDay(), 
            today.atTime(23, 59, 59)
        );
        
        // 첫 번째 조회에서 연속일수 계산
        Set<LocalDate> recordedDates = posts.stream()
            .filter(post -> post.getStatus() == com.coredisc.domain.common.enums.PostStatus.PUBLISHED)
            .map(post -> post.getCreatedAt().toLocalDate())
            .collect(Collectors.toSet());
        
        // 오늘부터 이전 날짜로 거슬러 올라가면서 연속 기록 여부 확인
        LocalDate currentDate = today;
        while (recordedDates.contains(currentDate)) {
            continuousDays++;
            currentDate = currentDate.minusDays(1);
        }
        
        // 만약 100일을 모두 연속으로 기록했다면, 추가 조회
        if (continuousDays == batchSize) {
            // 100일 전부터 200일 전까지 추가 조회
            LocalDate nextStartDate = startDate.minusDays(batchSize);
            List<Post> additionalPosts = postRepository.findAllByMemberAndCreatedAtBetweenOrderByCreatedAtAsc(
                member, 
                nextStartDate.atStartOfDay(), 
                startDate.minusDays(1).atTime(23, 59, 59)
            );
            
            Set<LocalDate> additionalDates = additionalPosts.stream()
                .filter(post -> post.getStatus() == com.coredisc.domain.common.enums.PostStatus.PUBLISHED)
                .map(post -> post.getCreatedAt().toLocalDate())
                .collect(Collectors.toSet());
            
            // 추가 연속일수 계산
            currentDate = startDate.minusDays(1);
            while (additionalDates.contains(currentDate)) {
                continuousDays++;
                currentDate = currentDate.minusDays(1);
                
                // 최대 365일까지 체크
                if (continuousDays >= 365) {
                    break;
                }
            }
        }
        
        return continuousDays;
    }
}