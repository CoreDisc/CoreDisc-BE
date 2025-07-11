package com.coredisc.application.service.follow;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;

public interface FollowCommandService {

    // 팔로우 하기
    Follow follow(Member member, Long targetId);

    // 언팔로우 하기
    void unfollow(Member member, Long targetId);
}
