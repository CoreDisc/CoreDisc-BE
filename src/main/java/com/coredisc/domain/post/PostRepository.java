package com.coredisc.domain.post;

import com.coredisc.domain.common.enums.PublicityType;
import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface PostRepository {

    // QueryDSL
    List<Post> findMyPostsWithAnswers(Member member, Long cursorId, Pageable pageable);
    List<Post> findUserPostsWithAnswers(Member member, boolean isCircle, Long cursorId, Pageable pageable);
    boolean existsByMemberAndIdLessThan(Member member, Long id, Set<PublicityType> allowTypes);
}
