package com.coredisc.domain.post;


import com.coredisc.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_answer_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostAnswerImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(name ="thumbnail_url",nullable =false)
    private String thumbnailUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_answer_id",nullable = false)
    private PostAnswer postAnswer;

}