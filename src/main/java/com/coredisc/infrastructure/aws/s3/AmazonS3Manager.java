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
import java.util.UUID;

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
     * 파일 검증
     */
    private void validateFile(MultipartFile file) {
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
    private String generateFileKey(Long memberId) {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        return String.format("user_%d_%s", memberId, uuid);
    }


    /**
     * S3에 원본 이미지 업로드
     * @return 원본 이미지 url
     */
    private String uploadToS3(MultipartFile file, String s3Key) throws IOException {
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
