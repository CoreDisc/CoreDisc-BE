package com.coredisc.infrastructure.file;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileInfo {
    private final String originalFileName;    // 원본 파일명
    private final String storedFileName;      // 서버 저장 파일명
    private final String fileExtension;       // 파일 확장자
    private final long fileSize;              // 파일 크기
    private final String filePath;            // 서버 내 파일 경로
    private final String fileUrl;             // 접근 가능한 URL
    private final String thumbnailPath;       // 썸네일 경로 (선택)
    private final String thumbnailUrl;        // 썸네일 URL (선택)
}
