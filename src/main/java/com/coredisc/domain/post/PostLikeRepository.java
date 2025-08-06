package com.coredisc.domain.post;

import com.coredisc.domain.member.Member;


public interface PostLikeRepository{

    boolean existsByMemberAndPost(Member member, Post post);

    PostLike createPostLike(PostLike postLike);
}
