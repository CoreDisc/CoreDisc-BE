package com.coredisc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file.storage")
@Getter
@Setter
public class FileStorageProperties {

    private String uploadPath = "uploads"; // 기본 업로드 경로
    private String baseUrl = "http://localhost:8080"; // 기본 서버 URL
    private String imageDirectory = "images"; // 이미지 디렉토리
    private String thumbnailDirectory = "thumbnails"; // 썸네일 디렉토리
    private long maxFileSize = 10 * 1024 * 1024; // 10MB
    private String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "webp"};
}
