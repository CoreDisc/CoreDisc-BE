package com.coredisc.infrastructure.repository.answer;

import com.coredisc.domain.post.PostAnswerImage;
import com.coredisc.domain.post.PostAnswerImageRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImageRepositoryAdaptor implements PostAnswerImageRepository {
    private final JpaPostImageRepository jpaPostImageRepository;

    @Override
    public void delete(PostAnswerImage postAnswerImage) {
        jpaPostImageRepository.delete(postAnswerImage);
    }
}
