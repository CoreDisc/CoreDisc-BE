package com.coredisc.application.service.member;


import com.coredisc.domain.member.Member;

public interface MemberQueryService {
    Member getMemberByUsername(String username);
}
