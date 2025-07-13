package com.coredisc.infrastructure.file;


import org.springframework.web.multipart.MultipartFile;

public interface FileStore {

    /**
     * 파일 저장
     */
    FileInfo storeFile(MultipartFile file, String directory);

    /**
     * 파일 삭제
     */
    void deleteFile(String filePath);

    /**
     * 썸네일 생성
     */
    String createThumbnail(String originalImagePath, int width, int height);

    /**
     * 파일 존재 여부 확인
     */
    boolean exists(String filePath);

    /**
     * 파일 URL 생성
     */
    String generateFileUrl(String filePath);
}
