package com.coredisc.infrastructure.repository.member.querydsl;

import com.coredisc.domain.member.Member;

import java.util.List;

public interface QueryMemberRepository {

    // 검색 화면 사용자 검색
    List<Member> findMemberListByKeyword(
            Member member,
            String keyword,
            Long cursorId,
            int pageSize
    );
}
