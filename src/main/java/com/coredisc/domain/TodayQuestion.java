package com.coredisc.domain;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TodayQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    protected LocalDateTime selectedDate;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private QuestionType questionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "official_question_id")
    private OfficialQuestion officialQuestion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_question_id")
    private PersonalQuestion personalQuestion;
}
