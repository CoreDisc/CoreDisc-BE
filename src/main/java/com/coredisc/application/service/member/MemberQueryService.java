package com.coredisc.application.service.member;


import com.coredisc.domain.member.Member;

public interface MemberQueryService {
    // CurrentMemberResolver를 위해 Member 찾기
    Member getMemberByUsername(String username);
}
