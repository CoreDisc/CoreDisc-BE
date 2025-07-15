package com.coredisc.infrastructure.repository.post;

import com.coredisc.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostRepository extends JpaRepository<Post, Long> {
}
