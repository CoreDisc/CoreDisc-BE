package com.coredisc.infrastructure.repository.post;

import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostRepositoryAdaptor implements PostRepository {

    private final JpaPostRepository jpaPostRepository;


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



}
