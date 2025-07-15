package com.coredisc.domain.disc;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.DiscCoverColor;
import com.coredisc.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(
        name = "disc",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"year", "month", "member_id"})
        }
)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Disc extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    @Min(1) @Max(12)
    private int month;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscCoverColor coverColor;

    @Setter
    @Column(nullable = true)
    private String coverImgUrl;

    // 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public boolean hasCoverImage() {
        return coverImgUrl != null && !coverImgUrl.isEmpty();
    }
}
