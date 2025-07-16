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

    // 접근 가능한 URL
    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    // 파일 메타데이터
    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "stored_file_name")
    private String storedFileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private Long fileSize;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_answer_id", nullable = false)
    private PostAnswer postAnswer;

    // ======================== 비즈니스 로직 ========================

    /**
     * 이미지 정보 업데이트
     */
    public void updateImageInfo(String imgUrl, String thumbnailUrl, String originalFileName,
                                String storedFileName, String filePath, Long fileSize) {
        this.imgUrl = imgUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    /**
     * 썸네일이 있는지 확인
     */
    public boolean hasThumbnail() {
        return thumbnailUrl != null && !thumbnailUrl.trim().isEmpty();
    }

    /**
     * 파일 크기를 MB 단위로 반환
     */
    public double getFileSizeInMB() {
        if (fileSize == null) return 0.0;
        return fileSize / (1024.0 * 1024.0);
    }

}