package com.coredisc.domain;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.QuestionType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.personalQuestion.PersonalQuestion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "todayQuestion")
    private List<PostAnswer> postAnswers = new ArrayList<>();
}
