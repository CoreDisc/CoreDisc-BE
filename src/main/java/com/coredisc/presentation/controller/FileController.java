package com.coredisc.presentation.controller;

import com.coredisc.config.FileStorageProperties;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController  {
    private final FileStorageProperties fileStorageProperties;
    /**
     * 파일 다운로드/조회
     */
    @GetMapping("/**")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
        try {
            // 요청 경로에서 파일 경로 추출
            String requestPath = request.getRequestURI().substring("/files/".length());

            // 파일 경로 생성
            Path filePath = Paths.get(fileStorageProperties.getUploadPath()).resolve(requestPath);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                log.warn("파일을 찾을 수 없음: {}", requestPath);
                return ResponseEntity.notFound().build();
            }

            // Content-Type 설정
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            log.error("파일 다운로드 실패", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
