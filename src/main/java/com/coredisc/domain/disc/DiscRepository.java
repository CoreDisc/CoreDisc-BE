package com.coredisc.domain.disc;

import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DiscRepository {
    Disc save(Disc disc);
    Long countByMember(Member member);
    Page<Disc> findByMember(Member member, Pageable pageable);
    List<Disc> findAllByMemberOrderByYearDescMonthDesc(Member member);
    Optional<Disc> findByIdAndMember(Long id, Member member);
    boolean existsByMemberAndYearAndMonth(Member member, int year, int month);
    boolean existsByMemberIdAndYearAndMonth(Long memberId, int year, int month);
}