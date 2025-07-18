package com.coredisc.domain.member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByNameAndEmail(String name, String email);
    boolean existsByNameAndUsername(String name, String username);
    Optional<Member> findByNameAndUsername(String name, String username);
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long followerId);
}
