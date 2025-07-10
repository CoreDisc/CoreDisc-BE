package com.coredisc.domain.member;

public interface MemberRepository {

    Member save(Member member);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
