package com.coredisc.infrastructure.aws.s3;


import com.coredisc.domain.common.enums.AnswerType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImageUploadResult {
    private String originalUrl;        // S3 CDN URL (원본)
    private String thumbnailUrl;    // S3 썸네일 URL (선택사항)
    private String originalKey;
    private String thumbnailKey;
    private String originalFileName; // 사용자가 업로드한 원본 파일명
}
