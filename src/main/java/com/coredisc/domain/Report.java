package com.coredisc.domain;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "report",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_member_year_month",
                columnNames = {"member_id", "year", "month"}
        ))
public class Report extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Min(2025)
    private int year;

    @Column(nullable = false)
    @Min(1) @Max(12)
    private int month;

    private String coverImgUrl;

    public String getReportTitle() {
        return year + "년 " + month + "월 리포트";
    }
}