package com.coredisc.presentation.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class AuthResponseDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SignupResultDTO {

        private Long id;

        private LocalDateTime createdAt;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CheckNicknameResultDTO {

        private boolean isDuplicated;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CheckUsernameResultDTO {

        private boolean isDuplicated;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CheckEmailResultDTO {

        private boolean isDuplicated;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class VerifyCodeResultDTO {

        private boolean isVerified;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LoginResultDTO {

        private Long id;

        private String accessToken;

        private String refreshToken;
    }
}
