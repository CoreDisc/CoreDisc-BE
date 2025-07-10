package com.coredisc.common.converter;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;

public class FollowConverter {

    //TODO : isCircle -> false 디폴트로 수정
    public static Follow toFollow(Member follower, Member following){

        return Follow.builder()
                .follower(follower)
                .following(following)
                .isCircle(false)
                .build();
    }


}
