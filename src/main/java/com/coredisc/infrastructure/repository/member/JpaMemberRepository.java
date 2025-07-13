package com.coredisc.infrastructure.repository.member;

import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findByNameAndEmail(String name, String email);
    boolean existsByNameAndUsername(String name, String username);
    Optional<Member> findByNameAndUsername(String name, String username);
    Optional<Member> findByEmail(String email);
}
