package com.coredisc.domain;


import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.common.enums.PublicityType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PublicityType publicity;  // PUBLIC, PRIVATE, CIRCLE

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PostStatus status = PostStatus.TEMP;  // TEMP, PUBLISHED

    // 선택형 일기
    @Column(name = "daily_who", length = 50)
    private String dailyWho;

    @Column(name = "daily_where", length = 50)
    private String dailyWhere;

    @Column(name = "daily_what", length = 50)
    private String dailyWhat;

    @Column(name = "daily_detail", length = 50)
    private String dailyDetail;  // mood로 변경 고려

    // 통계 (반정규화)
    @Column(name = "like_count")
    @Builder.Default
    private Integer likeCount = 0;

    @Column(name = "comment_count")
    @Builder.Default
    private Integer commentCount = 0;

    @Column(name = "view_count")
    @Builder.Default
    private Integer viewCount = 0;

    // 연관관계
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostAnswer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostLike> likes = new ArrayList<>();


}
