package com.coredisc.infrastructure.aws.s3;


import com.coredisc.domain.common.enums.AnswerType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ImageUploadResult {
    private String originalUrl;
    private String thumbnailUrl;
}
