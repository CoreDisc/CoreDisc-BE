package com.coredisc.infrastructure.repository.member;

import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findByNameAndEmail(String name, String email);
    boolean existsByEmailAndUsername(String email, String username);
    Optional<Member> findByEmailAndUsername(String email, String username);
    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.status = true")
    Page<Member> findAllForDiscCreation(Pageable pageable);
}
