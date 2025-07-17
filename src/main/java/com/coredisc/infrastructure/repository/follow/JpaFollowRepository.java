package com.coredisc.infrastructure.repository.follow;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface JpaFollowRepository extends JpaRepository<Follow, Long> {

    Long countByFollowerId(Long followerId);
    Long countByFollowingId(Long memberId);
    boolean existsByFollowerAndFollowing(Member follower, Member following);
    Follow findByFollowerAndFollowing(Member follower, Member following);
    List<Follow> findAllByFollowing(Member member);
    List<Follow> findAllByFollower(Member member);
    boolean existsByFollowerAndFollowingAndIsCircle(Member follower, Member following, boolean isCircle);

}
