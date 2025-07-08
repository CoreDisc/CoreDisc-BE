package com.coredisc.domain;



import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_answer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "question_id", nullable = false)
    private Integer questionId;  // 0, 1, 2, 3

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AnswerType answerType;  // IMAGE, TEXT

    // 텍스트 답변인 경우
    @Column(columnDefinition = "TEXT")
    private String textContent;

    // TODO: todayQuestion과의 관계 매핑
}