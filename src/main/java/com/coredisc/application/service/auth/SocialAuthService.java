package com.coredisc.application.service.auth;

import com.coredisc.presentation.dto.auth.AuthRequestDTO;
import com.coredisc.presentation.dto.auth.AuthResponseDTO;

public interface SocialAuthService {

    AuthResponseDTO.LoginResultDTO login(String provider, AuthRequestDTO.SocialLoginDTO request);
}
