package com.coredisc.application.service.member;

import com.coredisc.domain.Member;

public interface MemberQueryService {
    Member getMemberByUsername(String username);
}
