package com.coredisc.domain.mapping.memberOfficialQuestion;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberOfficialQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)   // 즐겨찾기. false = 즐겨찾기 안함, true = 즐겨찾기 함
    @Builder.Default
    private Boolean isFavorite = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "official_question_id")
    private OfficialQuestion officialQuestion;

    public void updateFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

}
