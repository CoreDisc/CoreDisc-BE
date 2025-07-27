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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_answer_id", nullable = false)
    private PostAnswer postAnswer;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;  // S3 원본 이미지 URL

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;  // S3 썸네일 URL (선택사항)

    @Column(name = "s3_key")
    private String s3Key;  // S3 객체 키 (삭제 시 필요)

    @Column(name = "original_file_name")
    private String originalFileName;  // 원본 파일명 (사용자가 업로드한 파일명)



    // === 비즈니스 로직 ===

    /**
     * 이미지 URL 업데이트
     */
    public void updateImageUrls(String imgUrl, String thumbnailUrl, String s3Key) {
        this.imgUrl = imgUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.s3Key = s3Key;
    }




}