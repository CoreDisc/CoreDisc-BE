package com.coredisc.application.service.member;


import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.member.MemberResponseDTO;

public interface MemberQueryService {

    // CurrentMemberResolver를 위해 Member 찾기
    Member getMemberByUsername(String username);

    // 마이홈(본인) 사용자 정보 확인
    MemberResponseDTO.MyHomeUserInfoDTO getMyHomeUserInfo(Member member);
}
