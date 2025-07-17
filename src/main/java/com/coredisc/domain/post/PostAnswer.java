package com.coredisc.domain.post;


import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.AnswerType;
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
    private String questionContent;

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



    // 비지니스 로직

    // 텍스트 답변 업데이트
    public void updateTextAnswer(String content) {
        this.type = AnswerType.TEXT;
        this.textContent = content;
        // 기존 이미지 제거
        this.postAnswerImage =null;
    }

    // 이미지 답변 업데이트
    public void updateToImageAnswer(PostAnswerImage image) {
        this.type = AnswerType.IMAGE;
        this.textContent = null;
        this.postAnswerImage = image;
    }

    //이미지 답변인지 확인
    public boolean isImageAnswer() {
        return this.type == AnswerType.IMAGE;
    }

    //텍스트 답변인지 확인
    public boolean isTextAnswer() {
        return this.type == AnswerType.TEXT;
    }

}