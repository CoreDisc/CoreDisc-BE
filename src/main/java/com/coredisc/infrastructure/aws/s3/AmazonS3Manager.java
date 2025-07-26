package com.coredisc.infrastructure.aws.s3;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.PostHandler;
import com.coredisc.config.S3Config;
import com.coredisc.infrastructure.file.FileInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;

    private final S3Config s3Config;

    public FileInfo uploadFile(MultipartFile file, Long memberId) {

        // 파일 검증
        validateFile(file);

        try {
            // 파일 키 생성
            String fileKey =generateFileKey(memberId);

            // 1. Original 이미지 업로드
            String originalKey =  "original/" + fileKey + ".jpg";
            String originalUrl = uploadToS3(file, originalKey);


            // 2. Thumbnail 이미지 생성 및 업로드
            String thumbnailKey =  "thumbnail/" + fileKey + ".jpg";
            String thumbnailUrl = uploadThumbnailToS3(file, thumbnailKey);

            log.info("이미지 업로드 완료 - 사용자: {}, 파일키: {}", memberId, fileKey);

            return FileInfo.builder()
                    .fileUrl(originalUrl)
                    .thumbnailUrl(thumbnailUrl)
                    .build();

        } catch (Exception e) {
            log.error("이미지 업로드 실패 - 사용자: {}, 파일명: {}", memberId, file.getOriginalFilename(), e);
            throw new RuntimeException("이미지 업로드 실패", e);
        }
    }

    public void deleteImage(String key) {
        try {
            // Original 삭제
            String originalKey = "original/" + key + ".jpg";
            deleteFromS3(originalKey);
            // Thumbnail 삭제
            String thumbnailKey =  "thumbnail/" + key + ".jpg";
            deleteFromS3(thumbnailKey);
        } catch(Exception e) {
            log.error("이미지 삭제 실패 - 파일키: {}", key, e);
        }
    }
    /**
     * URL 기반 이미지 삭제 (새로 추가)
     * @param imageUrl S3 전체 URL (예: https://bucket.s3.region.amazonaws.com/original/user_1_abc123.jpg)
     */
    public void deleteImageByUrl(String imageUrl) {
        try {
            // URL에서 파일키 추출
            String fileKey = extractFileKeyFromUrl(imageUrl);

            if (fileKey == null) {
                log.warn("URL에서 파일키 추출 실패: {}", imageUrl);
                return;
            }

            // 파일키로 이미지 삭제
            deleteImage(fileKey);

        } catch (Exception e) {
            log.error("URL 기반 이미지 삭제 실패 - URL: {}", imageUrl, e);
            throw new RuntimeException("URL 기반 이미지 삭제 실패", e);
        }
    }

    /**
     * 단일 파일 삭제 (S3 key 직접 삭제)
     */
    public void deleteImageByKey(String s3Key) {
        try {
            deleteFromS3(s3Key);
            log.info("S3 파일 삭제 완료 - 키: {}", s3Key);
        } catch (Exception e) {
            log.error("S3 파일 삭제 실패 - 키: {}", s3Key, e);
            throw new RuntimeException("S3 파일 삭제 실패", e);
        }
    }

    /**
     * URL에서 파일키 추출
     * URL 형식: https://bucket.s3.region.amazonaws.com/original/user_1_abc123.jpg
     * 추출 결과: user_1_abc123
     */
    public String extractFileKeyFromUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        try {
            // 정규식으로 파일키 패턴 추출
            // original/ 또는 thumbnail/ 다음의 user_숫자_문자열 부분을 추출
            Pattern pattern = Pattern.compile("(?:original|thumbnail|profiles)/(user_\\d+_[a-zA-Z0-9]+)\\.jpg");
            Matcher matcher = pattern.matcher(imageUrl);

            if (matcher.find()) {
                String fileKey = matcher.group(1);
                log.debug("URL에서 파일키 추출 성공 - URL: {}, 파일키: {}", imageUrl, fileKey);
                return fileKey;
            }

            // 정규식 실패 시 수동 파싱 시도
            return extractFileKeyManually(imageUrl);

        } catch (Exception e) {
            log.error("파일키 추출 중 오류 발생 - URL: {}", imageUrl, e);
            return null;
        }
    }

    /**
     * 수동으로 파일키 추출 (정규식 실패 시 백업)
     */
    private String extractFileKeyManually(String imageUrl) {
        try {
            // URL에서 경로 부분만 추출
            String path = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

            // .jpg 제거
            if (path.endsWith(".jpg")) {
                path = path.substring(0, path.length() - 4);
            }

            // user_로 시작하는지 확인
            if (path.startsWith("user_")) {
                log.debug("수동 파일키 추출 성공 - 파일키: {}", path);
                return path;
            }

            log.warn("파일키 패턴이 맞지 않음 - 추출된 값: {}", path);
            return null;

        } catch (Exception e) {
            log.error("수동 파일키 추출 실패 - URL: {}", imageUrl, e);
            return null;
        }
    }

    /**
     * URL이 현재 S3 버킷의 URL인지 검증
     */
    public boolean isValidS3Url(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        String expectedDomain = String.format("https://%s.s3.%s.amazonaws.com/",
                s3Config.getBucket(), s3Config.getRegion());

        return url.startsWith(expectedDomain);
    }

    /**
     * 여러 URL을 한번에 삭제
     */
    public void deleteImagesByUrls(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        log.info("다중 이미지 삭제 시작 - 개수: {}", imageUrls.size());

        int successCount = 0;
        int failCount = 0;

        for (String imageUrl : imageUrls) {
            try {
                if (isValidS3Url(imageUrl)) {
                    deleteImageByUrl(imageUrl);
                    successCount++;
                } else {
                    log.warn("유효하지 않은 S3 URL 건너뛰기: {}", imageUrl);
                    failCount++;
                }
            } catch (Exception e) {
                log.error("개별 이미지 삭제 실패 - URL: {}", imageUrl, e);
                failCount++;
            }
        }

        log.info("다중 이미지 삭제 완료 - 성공: {}, 실패: {}", successCount, failCount);
    }

    /**
     * 파일 검증
     */
    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new PostHandler(ErrorStatus.FILE_NOT_FOUND);
        }

        if (file.getSize() > 10 * 1024 * 1024) { // 10MB
            throw new PostHandler(ErrorStatus.FILE_SIZE_EXCEEDED);
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new PostHandler(ErrorStatus.INVALID_FILE_TYPE);
        }
    }


    /**
     * 파일키 생성 (user_memberId_uuid)
     */
    public String generateFileKey(Long memberId) {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        return String.format("user_%d_%s", memberId, uuid);
    }


    /**
     * S3에 원본 이미지 업로드
     * @return 원본 이미지 url
     */
    public String uploadToS3(MultipartFile file, String s3Key) throws IOException {
        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        // 버켓에 저장
        try {
            amazonS3.putObject(new PutObjectRequest(s3Config.getBucket(), s3Key, file.getInputStream(), metadata));
        } catch (IOException e) {
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return generateS3Url(s3Key);
    }


    /**
     * S3에 썸네일 이미지 업로드
     * @return 썸네일 url
     */
    private String uploadThumbnailToS3(MultipartFile file, String s3Key) throws IOException {
        // 1) 썸네일 생성
        BufferedImage thumbnail = createThumbnail(file, 800, 800);

        // 2) BufferedImage → byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, "jpg", baos);
        byte[] thumbnailBytes = baos.toByteArray();

        // 3) 메타데이터
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(thumbnailBytes.length);
        metadata.setContentType("image/jpeg");

        // 4) S3 업로드
        try (ByteArrayInputStream bais = new ByteArrayInputStream(thumbnailBytes)) {
            amazonS3.putObject(new PutObjectRequest(
                    s3Config.getBucket(),
                    s3Key,
                    bais,
                    metadata
            ));
        }

        return generateS3Url(s3Key);
    }

    /**
     * 썸네일 생성 (비율 유지)
     */
    private BufferedImage createThumbnail(MultipartFile file, int maxWidth, int maxHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        // 비율 계산
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        // 썸네일 생성
        BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = thumbnail.createGraphics();

        // 고품질 리사이징
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        g.dispose();

        return thumbnail;
    }

    /**
     * S3에서 파일 삭제
     */
    private void deleteFromS3(String s3Key) {
        DeleteObjectRequest request = new  DeleteObjectRequest(
                s3Config.getBucket(), s3Key
                );

        amazonS3.deleteObject(request);
    }

    /**
     * S3 URL 생성
     */
    private String generateS3Url(String s3Key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s",
                s3Config.getBucket(), s3Config.getRegion(), s3Key);
    }
}
