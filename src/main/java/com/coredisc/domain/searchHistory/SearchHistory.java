package com.coredisc.domain.searchHistory;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.SearchType;
import com.coredisc.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SearchHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Enumerated(EnumType.STRING)
    private SearchType searchType;

    @Column(nullable = false)
    private LocalDateTime searchedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updateTimestamp() {
        this.searchedAt = LocalDateTime.now();
    }

}
