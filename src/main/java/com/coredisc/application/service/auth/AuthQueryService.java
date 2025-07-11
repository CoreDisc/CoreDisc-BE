package com.coredisc.application.service.auth;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.auth.AuthRequestDTO;

public interface AuthQueryService {

    // username 중복 검사
    boolean checkUsername(String username);

    // email 중복 검사
    boolean checkEmail(String Email);

    // nickname 중복 검사
    boolean checkNickname(String nickname);

    // 아이디(username) 찾기
    Member findUsername(AuthRequestDTO.FindUsernameDTO request);

}
