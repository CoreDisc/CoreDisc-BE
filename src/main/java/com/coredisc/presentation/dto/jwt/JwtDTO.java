package com.coredisc.presentation.dto.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtDTO {
    private String accessToken;
    private String refreshToken;
}
