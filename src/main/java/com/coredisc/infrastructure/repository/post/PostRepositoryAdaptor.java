package com.coredisc.infrastructure.repository.post;


import com.coredisc.domain.common.enums.PublicityType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.infrastructure.repository.post.queryDsl.QueryPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
@RequiredArgsConstructor
public class PostRepositoryAdaptor implements PostRepository {

    private final JpaPostRepository jpaPostRepository;
    private final QueryPostRepository queryPostRepository;

    @Override
    public Post save(Post post) {
        return jpaPostRepository.save(post);
    }

    @Override
    public Optional<Post> findById(Long id) {
        return jpaPostRepository.findById(id);
    }

    @Override
    public void delete(Post post) {
        jpaPostRepository.delete(post);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Post> findMyPostsWithAnswers(Member member, Long cursorId, Pageable pageable) {
        return queryPostRepository.findMyPostsWithAnswers(member, cursorId, pageable);
    }

    @Override
    public List<Post> findUserPostsWithAnswers(Member member, boolean isCircle, Long cursorId, Pageable pageable) {
        return queryPostRepository.findUserPostsWithAnswers(member, isCircle, cursorId, pageable);
    }

    @Override
    public boolean existsByMemberAndIdLessThan(Member member, Long id, Set<PublicityType> allowTypes) {
        return queryPostRepository.existsByMemberAndIdLessThan(member, id, allowTypes);
    }

}
