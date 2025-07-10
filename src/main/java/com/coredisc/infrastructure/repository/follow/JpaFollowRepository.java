package com.coredisc.infrastructure.repository.follow;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(Member follower, Member following);
}
