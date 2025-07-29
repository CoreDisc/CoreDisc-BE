package com.coredisc.domain.reportStats;

import com.coredisc.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "daily_random_question_stat",
        indexes = @Index(name = "idx_member_date", columnList = "memberId, selectedDate"),
        uniqueConstraints = @UniqueConstraint(name = "uq_member_selected_date", columnNames = {"member_id", "selected_date"}))
public class DailyRandomQuestionStat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private LocalDate selectedDate;

    @Column(nullable = false, length = 50)
    private String questionContent;
}