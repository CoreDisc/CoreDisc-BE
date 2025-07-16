package com.coredisc.infrastructure.repository.postAnswerImage;

import com.coredisc.domain.post.PostAnswerImage;
import com.coredisc.infrastructure.repository.postAnswerImage.queryDsl.QueryPostAnswerImageRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostAnswerImageRepository extends JpaRepository<PostAnswerImage, Long> , QueryPostAnswerImageRepository {

    Boolean existsByIdLessThan(Long id);

}
