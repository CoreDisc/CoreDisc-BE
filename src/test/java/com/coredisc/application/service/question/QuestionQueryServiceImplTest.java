package com.coredisc.application.service.question;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.todayQuestion.TodayQuestionRepository;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuestionQueryServiceImplTest {

    @InjectMocks
    private QuestionQueryServiceImpl questionQueryServiceImpl;  // 가짜 객체 주입

    @Mock
    private TodayQuestionRepository todayQuestionRepository;    // 가짜 객체 생성 

    private Member testMember;

    @BeforeEach
    void setUp() {  // 초기 세팅
        testMember = Member.builder()
                .id(1L)
                .nickname("tester")
                .build();
    }

    @Test
    void getMyTodayQuestion_questionsExist() {

        // given: TodayQuestion 4개 생성
        TodayQuestion tq1 = TodayQuestion.builder().id(1L).questionOrder(1).build();
        TodayQuestion tq2 = TodayQuestion.builder().id(2L).questionOrder(2).build();
        TodayQuestion tq3 = TodayQuestion.builder().id(3L).questionOrder(3).build();
        TodayQuestion tq4 = TodayQuestion.builder().id(4L).questionOrder(4).build();

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        // order 1~3 : 한달 질문
        when(todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(testMember, 1, startOfMonth, endOfMonth))
                .thenReturn(Optional.of(tq1));
        when(todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(testMember, 2, startOfMonth, endOfMonth))
                .thenReturn(Optional.of(tq2));
        when(todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDateBetween(testMember, 3, startOfMonth, endOfMonth))
                .thenReturn(Optional.of(tq3));

        // order 4 : 오늘 질문
        when(todayQuestionRepository.findByMemberAndQuestionOrderAndSelectedDate(testMember, 4, today))
                .thenReturn(Optional.of(tq4));

        // when
        List<QuestionResponseDTO.SelectedTodayQuestionResultDTO> result =
                questionQueryServiceImpl.getMyTodayQuestion(testMember);

        // then
        assertThat(result).hasSize(4);
        assertThat(result).allSatisfy(dto -> assertThat(dto).isNotNull());

        result.forEach(dto -> System.out.println("TodayQuestion id: " + dto.getId()));
    }
}
