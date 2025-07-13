package com.coredisc.infrastructure.repository.answer;

import com.coredisc.domain.post.PostAnswerImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostImageRepository extends JpaRepository<PostAnswerImage,Long> {
}
