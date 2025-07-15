package com.coredisc.domain.post;

import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepository {

    // QueryDSL
    List<Post> findMyPostsWithAnswers(Member member, Long cursorId, Pageable pageable);

    boolean existsByMemberAndIdLessThan(Member member, Long id);
}
