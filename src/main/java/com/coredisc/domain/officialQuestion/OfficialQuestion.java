package com.coredisc.domain.officialQuestion;

import com.coredisc.domain.todayQuestion.TodayQuestion;
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
public class OfficialQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Builder.Default    // false: 기본질문, true: 공유질문
    private boolean isShared = true;

    @Column(nullable = false)   // 즐겨찾기. false = 즐겨찾기 안함, true = 즐겨찾기 함
    @Builder.Default
    private Boolean isFavorite = false;

    @Column(nullable = false)
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "officialQuestion")
    private List<QuestionCategory> questionCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "officialQuestion")
    private List<TodayQuestion> todayQuestionList = new ArrayList<>();

    public void updateFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

}
