package com.coredisc.infrastructure.repository.post;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.infrastructure.repository.post.queryDsl.QueryPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdaptor implements PostRepository {

    private final QueryPostRepository queryPostRepository;

    @Override
    public List<Post> findMyPostsWithAnswers(Member member, Long cursorId, Pageable pageable) {
        return queryPostRepository.findMyPostsWithAnswers(member, cursorId, pageable);
    }

    @Override
    public boolean existsByMemberAndIdLessThan(Member member, Long id) {
        return queryPostRepository.existsByMemberAndIdLessThan(member, id);
    }
}
