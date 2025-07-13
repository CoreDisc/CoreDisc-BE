package com.coredisc.domain.stats;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.PublicityType;
import com.coredisc.domain.common.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "daily_random_question_stat",
        indexes = @Index(name = "idx_member_date", columnList = "memberId, selectedDate"))
public class DailyRandomQuestionStat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private LocalDate selectedDate;

    @Column(nullable = false, length = 100) //질문 길이 얼마지??
    private String questionContent;
}