package com.coredisc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public static CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 허용할 출처
        configuration.setAllowedOriginPatterns(Arrays.asList(
                "http://localhost:3000"
                // TODO: 요청 허용 출처 추후 추가
        ));
        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        // 허용할 요청 헤더
        configuration.setAllowedHeaders(List.of("*"));
        // 클라이언트에게 노출할 응답 헤더
        configuration.addExposedHeader("*");  // 모든 응답 헤더 노출
        // 인증정보(쿠키, 헤더 등) 포함 요청 허용 여부
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
