package com.coredisc.infrastructure.repository.post.queryDsl;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryPostRepository {

    List<Post> findMyPostsWithAnswers(Member member, Long cursorId, Pageable pageable);

    boolean existsByMemberAndIdLessThan(Member member, Long id);
}
