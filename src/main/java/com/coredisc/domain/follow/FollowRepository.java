package com.coredisc.domain.follow;

import com.coredisc.domain.member.Member;

public interface FollowRepository {

    // 사용자의 팔로잉 수 구하기
    Long countByFollowerId(Long memberId);

    // 사용자의 팔로워 수 구하기
    Long countByFollowingId(Long memberId);

    // 팔로우 여부
    Boolean existsByFollowerAndFollowing(Member member, Member targetMember);

}
