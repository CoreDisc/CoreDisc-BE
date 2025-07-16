package com.coredisc.domain.officialQuestion;

import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.mapping.questionCategory.QuestionCategory;
import com.coredisc.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OfficialQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default    // false: 기본질문, true: 공유질문
    private boolean isOfficial = true;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "officialQuestion")
    private List<QuestionCategory> questionCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "officialQuestion")
    private List<TodayQuestion> todayQuestionList = new ArrayList<>();
}
