package com.coredisc.domain.post;

import com.coredisc.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostLikeRepository extends JpaRepository<PostLike,Long> {


    boolean existsByMemberAndPost(Member member, Post post);
}
