package com.coredisc.domain.follow;

import com.coredisc.domain.member.Member;

public interface FollowRepository {

    Follow save(Follow follow);
    boolean existsByFollowerAndFollowing(Member follower, Member following);
}
