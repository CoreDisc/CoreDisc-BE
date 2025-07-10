package com.coredisc.infrastructure.repository.follow;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaFollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowing(Member follower, Member following);
    Follow findByFollowerAndFollowing(Member follower, Member following);
    List<Follow> findAllByFollowing(Member member);
}
