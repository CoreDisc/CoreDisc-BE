package com.coredisc.infrastructure.repository.postAnswerImage;

import com.coredisc.domain.postAnswerImage.PostAnswerImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostAnswerImageRepository extends JpaRepository<PostAnswerImage, Long> {

    Boolean existsByIdLessThan(Long id);

}
