package com.coredisc.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class FileUtil {
    private static final List<String> IMAGE_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "webp", "bmp", "tiff"
    );

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    private FileUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 파일 확장자 추출
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }

    /**
     * 이미지 파일인지 확인
     */
    public static boolean isImageFile(String fileName) {
        String extension = getFileExtension(fileName);
        return IMAGE_EXTENSIONS.contains(extension);
    }

    /**
     * 이미지 파일인지 확인 (MultipartFile)
     */
    public static boolean isImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        // MIME 타입 확인
        String contentType = file.getContentType();
        if (contentType != null && contentType.startsWith("image/")) {
            return true;
        }

        // 파일 확장자 확인
        return isImageFile(file.getOriginalFilename());
    }

    /**
     * 파일 크기 검증
     */
    public static boolean isValidFileSize(MultipartFile file) {
        return file != null && file.getSize() <= MAX_FILE_SIZE;
    }

    /**
     * 안전한 파일명 생성
     */
    public static String sanitizeFileName(String fileName) {
        if (fileName == null) {
            return "unknown";
        }

        // 특수문자 제거 및 공백을 언더스코어로 변경
        return fileName.replaceAll("[^a-zA-Z0-9.\\-_]", "_")
                .replaceAll("\\s+", "_")
                .toLowerCase();
    }

    /**
     * 파일 크기를 읽기 쉬운 형태로 변환
     */
    public static String formatFileSize(long sizeInBytes) {
        if (sizeInBytes < 1024) {
            return sizeInBytes + " B";
        } else if (sizeInBytes < 1024 * 1024) {
            return String.format("%.1f KB", sizeInBytes / 1024.0);
        } else if (sizeInBytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", sizeInBytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", sizeInBytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
