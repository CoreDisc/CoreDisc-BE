package com.coredisc.infrastructure.repository.member;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.infrastructure.repository.member.querydsl.QueryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdaptor implements MemberRepository {

    private final JpaMemberRepository jpaMemberRepository;
    private final QueryMemberRepository queryMemberRepository;

    @Override
    public Member save(Member member) {
        return jpaMemberRepository.save(member);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaMemberRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaMemberRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return jpaMemberRepository.existsByNickname(nickname);
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return jpaMemberRepository.findByUsername(username);
    }

    @Override
    public Optional<Member> findByNameAndEmail(String name, String email) {
        return jpaMemberRepository.findByNameAndEmail(name, email);
    }

    @Override
    public boolean existsByEmailAndUsername(String email, String username) {
        return jpaMemberRepository.existsByEmailAndUsername(email, username);
    }

    @Override
    public Optional<Member> findByEmailAndUsername(String email, String username) {
        return jpaMemberRepository.findByEmailAndUsername(email, username);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return jpaMemberRepository.findByEmail(email);
    }

    @Override
    public Optional<Member> findById(Long memberId) { return jpaMemberRepository.findById(memberId); }

    @Override
    public Optional<Member> findByUsernameAndStatus(String username, Boolean status) {
        return jpaMemberRepository.findByUsernameAndStatus(username, status);
    }

    @Override
    public Page<Member> findAllForDiscCreation(Pageable pageable) {
        return jpaMemberRepository.findAllForDiscCreation(pageable);
    }

    // 검색 화면 사용자 검색
    @Override
    public List<Member> findMemberListByKeyword(
            String keyword,
            Long cursorId,
            int pageSize
    ) {
        return queryMemberRepository.findMemberListByKeyword(keyword, cursorId, pageSize);
    }

    @Override
    public List<Member> findAllById(Set<Long> receiverIds) {
        return jpaMemberRepository.findAllById(receiverIds);
    }

}
