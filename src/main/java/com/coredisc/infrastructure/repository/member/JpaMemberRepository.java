package com.coredisc.infrastructure.repository.member;

import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
