package com.coredisc.domain.disc;

import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DiscRepository {
    Disc save(Disc disc);
    Long countByMember(Member member);
    Page<Disc> findByMember(Member member, Pageable pageable);
    Optional<Disc> findByIdAndMember(Long id, Member member);
}