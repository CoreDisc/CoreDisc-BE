package com.coredisc.domain.member;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MemberRepository {

    Member save(Member member);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByNameAndEmail(String name, String email);
    boolean existsByEmailAndUsername(String email, String username);
    Optional<Member> findByEmailAndUsername(String email, String username);
    Optional<Member> findByEmail(String email);
    Optional<Member> findById(Long followerId);
    Optional<Member> findByUsernameAndStatus(String username, Boolean status);

    Page<Member> findAllForDiscCreation(Pageable pageable);

    // 검색 화면 사용자 검색
    List<Member> findMemberListByKeyword(
            String keyword,
            Long cursorId,
            int pageSize
    );

    List<Member> findAllById(Set<Long> receiverIds);
}
