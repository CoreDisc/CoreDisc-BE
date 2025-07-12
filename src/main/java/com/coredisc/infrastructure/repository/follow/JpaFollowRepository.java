package com.coredisc.infrastructure.repository.follow;

import com.coredisc.domain.follow.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFollowRepository extends JpaRepository<Follow, Long> {

    Long countByFollowerId(Long followerId);

    Long countByFollowingId(Long memberId);
}
