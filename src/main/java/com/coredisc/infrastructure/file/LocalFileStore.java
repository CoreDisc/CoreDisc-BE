package com.coredisc.infrastructure.file;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.PostHandler;
import com.coredisc.config.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocalFileStore implements FileStore {

    private final FileStorageProperties fileStorageProperties;

    @Override
    public FileInfo storeFile(MultipartFile file, String directory) {

        // 1. 파일 검증
        validateFile(file);

        // 2. 파일 정보 추출
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        String storedFileName = generateStoredFileName(fileExtension);

        // 3. 저장 경로 생성
        String relativePath = directory + "/" + getCurrentDatePath() + "/" + storedFileName;
        Path targetPath = Paths.get(fileStorageProperties.getUploadPath()).resolve(relativePath);

        try {
            // 4. 디렉토리 생성
            Files.createDirectories(targetPath.getParent());

            // 5. 파일 저장
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 6. 썸네일 생성 (이미지인 경우)
            String thumbnailPath = null;
            String thumbnailUrl = null;
            if (isImageFile(fileExtension)) {
                thumbnailPath = createThumbnail(targetPath.toString(), 300, 300);
                thumbnailUrl = generateFileUrl(thumbnailPath);
            }

            // 7. FileInfo 생성
            FileInfo fileInfo = FileInfo.builder()
                    .originalFileName(originalFileName)
                    .storedFileName(storedFileName)
                    .fileExtension(fileExtension)
                    .fileSize(file.getSize())
                    .filePath(relativePath)
                    .fileUrl(generateFileUrl(relativePath))
                    .thumbnailPath(thumbnailPath)
                    .thumbnailUrl(thumbnailUrl)
                    .build();

            log.info("파일 저장 완료 - 원본: {}, 저장명: {}, 경로: {}",
                    originalFileName, storedFileName, relativePath);

            return fileInfo;

        } catch (IOException e) {
            log.error("파일 저장 실패 - 파일명: {}", originalFileName, e);
            throw new PostHandler(ErrorStatus.FILE_STORE_FAILED);
        }
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            return;
        }

        try {
            Path targetPath = Paths.get(fileStorageProperties.getUploadPath()).resolve(filePath);

            if (Files.exists(targetPath)) {
                Files.delete(targetPath);
                log.info("파일 삭제 완료 - 경로: {}", filePath);

                // 썸네일도 함께 삭제
                deleteThumbnailIfExists(filePath);
            }

        } catch (IOException e) {
            log.error("파일 삭제 실패 - 경로: {}", filePath, e);
        }
    }

    @Override
    public String createThumbnail(String originalImagePath, int width, int height) {
        try {
            // 1. 원본 이미지 읽기
            BufferedImage originalImage = ImageIO.read(new File(originalImagePath));
            if (originalImage == null) {
                log.warn("이미지 파일을 읽을 수 없음: {}", originalImagePath);
                return null;
            }

            // 2. 썸네일 크기 계산 (비율 유지)
            Dimension thumbnailSize = calculateThumbnailSize(
                    originalImage.getWidth(), originalImage.getHeight(), width, height);

            // 3. 썸네일 생성
            BufferedImage thumbnailImage = new BufferedImage(
                    thumbnailSize.width, thumbnailSize.height, BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics = thumbnailImage.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(originalImage, 0, 0, thumbnailSize.width, thumbnailSize.height, null);
            graphics.dispose();

            // 4. 썸네일 저장 경로 생성
            String thumbnailPath = generateThumbnailPath(originalImagePath);
            Path thumbnailFilePath = Paths.get(thumbnailPath);
            Files.createDirectories(thumbnailFilePath.getParent());

            // 5. 썸네일 저장
            String formatName = getFileExtension(originalImagePath);
            if ("jpg".equals(formatName) || "jpeg".equals(formatName)) {
                formatName = "jpg";
            }

            ImageIO.write(thumbnailImage, formatName, thumbnailFilePath.toFile());

            // 6. 상대 경로 반환
            String relativeThumbnailPath = Paths.get(fileStorageProperties.getUploadPath())
                    .relativize(thumbnailFilePath).toString().replace("\\", "/");

            log.info("썸네일 생성 완료 - 원본: {}, 썸네일: {}", originalImagePath, relativeThumbnailPath);

            return relativeThumbnailPath;

        } catch (IOException e) {
            log.error("썸네일 생성 실패 - 원본: {}", originalImagePath, e);
            return null;
        }
    }

    @Override
    public boolean exists(String filePath) {
        if (filePath == null) return false;

        Path targetPath = Paths.get(fileStorageProperties.getUploadPath()).resolve(filePath);
        return Files.exists(targetPath);
    }

    @Override
    public String generateFileUrl(String filePath) {
        if (filePath == null) return null;

        return fileStorageProperties.getBaseUrl() + "/files/" + filePath.replace("\\", "/");
    }

    // ======================== Private Methods ========================

    /**
     * 파일 검증
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new PostHandler(ErrorStatus.EMPTY_FILE);
        }

        if (file.getSize() > fileStorageProperties.getMaxFileSize()) {
            throw new PostHandler(ErrorStatus.FILE_SIZE_EXCEEDED);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new PostHandler(ErrorStatus.INVALID_FILE_NAME);
        }

        String extension = getFileExtension(fileName);
        if (!isAllowedExtension(extension)) {
            throw new PostHandler(ErrorStatus.INVALID_FILE_TYPE);
        }
    }

    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }

    /**
     * 허용된 확장자인지 확인
     */
    private boolean isAllowedExtension(String extension) {
        for (String allowedExt : fileStorageProperties.getAllowedExtensions()) {
            if (allowedExt.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 이미지 파일인지 확인
     */
    private boolean isImageFile(String extension) {
        return isAllowedExtension(extension);
    }

    /**
     * 저장용 파일명 생성 (UUID + timestamp + 확장자)
     */
    private String generateStoredFileName(String extension) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return uuid + "_" + timestamp + "." + extension;
    }

    /**
     * 현재 날짜 기반 디렉토리 경로 생성 (yyyy/MM/dd)
     */
    private String getCurrentDatePath() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    /**
     * 썸네일 경로 생성
     */
    private String generateThumbnailPath(String originalImagePath) {
        Path originalPath = Paths.get(originalImagePath);
        Path parentDir = originalPath.getParent();
        String fileName = originalPath.getFileName().toString();
        String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = getFileExtension(fileName);

        // thumbnails 폴더를 만들고 _thumb 접미사 추가
        Path thumbnailDir = parentDir.resolve("thumbnails");
        return thumbnailDir.resolve(nameWithoutExt + "_thumb." + extension).toString();
    }

    /**
     * 썸네일 크기 계산 (비율 유지)
     */
    private Dimension calculateThumbnailSize(int originalWidth, int originalHeight, int maxWidth, int maxHeight) {
        double widthRatio = (double) maxWidth / originalWidth;
        double heightRatio = (double) maxHeight / originalHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int thumbnailWidth = (int) (originalWidth * ratio);
        int thumbnailHeight = (int) (originalHeight * ratio);

        return new Dimension(thumbnailWidth, thumbnailHeight);
    }

    /**
     * 썸네일 삭제
     */
    private void deleteThumbnailIfExists(String originalFilePath) {
        try {
            Path originalPath = Paths.get(fileStorageProperties.getUploadPath()).resolve(originalFilePath);
            Path thumbnailPath = Paths.get(generateThumbnailPath(originalPath.toString()));

            if (Files.exists(thumbnailPath)) {
                Files.delete(thumbnailPath);
                log.info("썸네일 삭제 완료 - 경로: {}", thumbnailPath);
            }
        } catch (IOException e) {
            log.error("썸네일 삭제 실패 - 원본 경로: {}", originalFilePath, e);
        }
    }
}