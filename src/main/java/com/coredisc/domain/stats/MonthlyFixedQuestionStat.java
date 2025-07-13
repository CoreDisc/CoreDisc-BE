package com.coredisc.domain.stats;

import com.coredisc.domain.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "monthly_fixed_question_stat",
        indexes = @Index(name = "idx_member_year_month", columnList = "memberId, year, month"))
public class MonthlyFixedQuestionStat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    @Min(1) @Max(12)
    private int month;

    @Column(nullable = false)
    @Min(0) @Max(3)
    private int questionOrder;

    @Column(nullable = false, length = 100)
    private String questionContent;
}
