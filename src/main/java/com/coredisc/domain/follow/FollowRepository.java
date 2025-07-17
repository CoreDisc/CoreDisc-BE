package com.coredisc.domain.follow;

import com.coredisc.domain.member.Member;

import java.util.List;

public interface FollowRepository {

    // 사용자의 팔로잉 수 구하기
    Long countByFollowerId(Long memberId);

    // 사용자의 팔로워 수 구하기
    Long countByFollowingId(Long memberId);

    // 팔로우 여부
    boolean existsByFollowerAndFollowing(Member follower, Member following);

    Follow save(Follow follow);
    Follow findByFollowerAndFollowing(Member follower, Member following);
    void delete(Follow follow);
    List<Follow> findAllByFollowing(Member member);
    List<Follow> findAllByFollower(Member member);

    // 써클 여부
    boolean existsByFollowerAndFollowingAndIsCircle(Member follower, Member following, boolean isCircle);
}
