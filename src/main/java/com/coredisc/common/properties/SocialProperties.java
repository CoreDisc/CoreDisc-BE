package com.coredisc.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "social") // yml 파일 social 아래 값을 바인딩
public class SocialProperties {

    private ProviderProperties kakao; // kakao 관련 값

    @Data
    public static class ProviderProperties {

        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String tokenUri;
        private String userInfoUri;
    }
}
