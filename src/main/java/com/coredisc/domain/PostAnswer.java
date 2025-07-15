package com.coredisc.domain;


import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.postAnswerImage.PostAnswerImage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_answer",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"post_id", "question_id"})
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswerType type;  // IMAGE, TEXT

    // 텍스트 답변인 경우
    @Column(length = 50)
    private String textContent;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private TodayQuestion todayQuestion; // 0,1,2,3

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 1:1 관계 (IMAGE 타입일때만 존재)
    @OneToOne(mappedBy = "postAnswer", cascade = CascadeType.ALL, orphanRemoval = true)
    private PostAnswerImage postAnswerImage;




}