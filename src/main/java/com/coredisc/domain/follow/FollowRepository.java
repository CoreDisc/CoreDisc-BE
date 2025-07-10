package com.coredisc.domain.follow;

import com.coredisc.domain.member.Member;

import java.util.List;

public interface FollowRepository {

    Follow save(Follow follow);
    boolean existsByFollowerAndFollowing(Member follower, Member following);
    Follow findByFollowerAndFollowing(Member follower, Member following);
    void delete(Follow follow);
    List<Follow> findAllByFollowing(Member member);
}
