package com.coredisc.domain.personalQuestion;

import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import com.coredisc.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PersonalQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "personalQuestion")
    private List<QuestionCategory> questionCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "personalQuestion")
    private List<TodayQuestion> todayQuestionList = new ArrayList<>();
}