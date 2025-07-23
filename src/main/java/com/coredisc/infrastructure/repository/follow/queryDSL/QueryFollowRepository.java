package com.coredisc.infrastructure.repository.follow.queryDSL;

import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryFollowRepository {

    List<Follow> findCircleFollowers(Member member, Long cursorId, Pageable pageable);

    int countCircleFollowers(Member member);
}
