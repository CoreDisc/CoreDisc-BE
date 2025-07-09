package com.coredisc.application.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthQueryServiceImpl implements AuthQueryService{

    // username 중복 검사
    @Override
    public boolean checkUsername(String username) {
        return true;
    }

    // email 중복 검사
    @Override
    public boolean checkEmail(String Email) {
        return true;
    }

    // nickname 중복 검사
    @Override
    public boolean checkNickname(String nickname) {
        return true;
    }
}
