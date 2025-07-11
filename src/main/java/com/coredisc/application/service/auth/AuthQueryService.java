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

    // 비밀번호 변경을 위한 사용자 검증
    boolean verifyUser(AuthRequestDTO.VerifyUserDTO request);

    // 사용자 찾기
    Member findMember(AuthRequestDTO.VerifyUserDTO request);

}
