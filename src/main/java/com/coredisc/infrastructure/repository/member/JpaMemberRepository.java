package com.coredisc.infrastructure.repository.member;

import com.coredisc.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberRepository extends JpaRepository<Member, Long> {
}
