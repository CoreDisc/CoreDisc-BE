package com.coredisc.infrastructure.repository.post;


import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.QMember;
import com.coredisc.domain.post.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface JpaPostRepository extends JpaRepository<Post, Long> {

    boolean existsByMemberAndStatusAndCreatedAtBetween(Member member, PostStatus status, LocalDateTime startOfDay, LocalDateTime endOfDay);

}




