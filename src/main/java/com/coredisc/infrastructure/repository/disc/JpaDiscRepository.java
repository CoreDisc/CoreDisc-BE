package com.coredisc.infrastructure.repository.disc;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.disc.Disc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaDiscRepository extends JpaRepository<Disc, Long> {
    Long countByMember(Member member);
    List<Disc> findByMember(Member member);
    Optional<Disc> findByIdAndMember(Long id, Member member);
}
