package com.coredisc.infrastructure.repository.disc;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.disc.Disc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDiscRepository extends JpaRepository<Disc, Long> {

    Long countByMember(Member member);
}
