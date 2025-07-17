package com.coredisc.infrastructure.repository.postAnswerImage;

import com.coredisc.domain.post.PostAnswerImage;
import com.coredisc.domain.postAnswerImage.PostAnswerImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class PostAnswerImageRepositoryAdaptor implements PostAnswerImageRepository {

    private final JpaPostAnswerImageRepository postAnswerImageRepository;


    @Override
    public void delete(PostAnswerImage postAnswerImage) {
        postAnswerImageRepository.delete(postAnswerImage);
    }
}
