package com.coredisc.domain.reportStats;

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
@Table(name = "monthly_selection_diary_stat",
        indexes = @Index(name = "idx_member_year_month_type", columnList = "memberId, year, month, dailyType"))
public class MonthlySelectionDiaryStat extends BaseEntity {
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
    @Min(1) @Max(3)
    private int dailyType; // 1, 2, 3 (데일리 질문 3개)

    @Column(nullable = false)
    private int selectedOption; // 1~5 (선택된 옵션)

    @Column(nullable = false)
    @Min(0)
    private int selectionCount; // 해당 옵션이 선택된 횟수
}
