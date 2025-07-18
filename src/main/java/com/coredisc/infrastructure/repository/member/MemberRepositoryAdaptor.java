package com.coredisc.infrastructure.repository.member;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdaptor implements MemberRepository {

    private final JpaMemberRepository jpaMemberRepository;

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
    public boolean existsByNameAndUsername(String name, String username) {
        return jpaMemberRepository.existsByNameAndUsername(name, username);
    }

    @Override
    public Optional<Member> findByNameAndUsername(String name, String username) {
        return jpaMemberRepository.findByNameAndUsername(name, username);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return jpaMemberRepository.findByEmail(email);
    }

    @Override
    public Optional<Member> findById(Long memberId) { return jpaMemberRepository.findById(memberId); }
}
