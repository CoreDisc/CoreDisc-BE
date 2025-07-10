package com.coredisc.application.service.auth;

public interface AuthQueryService {

    // username 중복 검사
    boolean checkUsername(String username);

    // email 중복 검사
    boolean checkEmail(String Email);

    // nickname 중복 검사
    boolean checkNickname(String nickname);

}
