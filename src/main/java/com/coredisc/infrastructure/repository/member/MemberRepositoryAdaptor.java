package com.coredisc.infrastructure.repository.member;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
