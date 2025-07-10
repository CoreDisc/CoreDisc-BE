package com.coredisc.application.service.follow;

import com.coredisc.domain.follow.Follow;

public interface FollowCommandService {

    // 팔로우 하기
    Follow follow(Long followerId, Long followingId);

    // 언팔로우 하기
    void unfollow(Long followerId, Long followingId);
}
