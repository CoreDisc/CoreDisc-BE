package com.coredisc.domain.disc;

import com.coredisc.domain.member.Member;

import java.util.List;
import java.util.Optional;

public interface DiscRepository {
    Disc save(Disc disc);
    Long countByMember(Member member);
    List<Disc> findByMember(Member member);
    Optional<Disc> findByIdAndMember(Long id, Member member);
}