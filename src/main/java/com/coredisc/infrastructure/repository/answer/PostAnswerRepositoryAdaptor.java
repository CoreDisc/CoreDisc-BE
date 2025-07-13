package com.coredisc.infrastructure.repository.answer;

import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import com.coredisc.domain.post.PostAnswerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class PostAnswerRepositoryAdaptor implements PostAnswerRepository {

    private final JpaPostAnswerRepository jpaPostAnswerRepository;

    @Override
    public PostAnswer save(PostAnswer postAnswer) {
        return jpaPostAnswerRepository.save(postAnswer);
    }

    @Override
    public Optional<PostAnswer> findById(Long id) {
        return jpaPostAnswerRepository.findById(id);
    }

    @Override
    public void delete(PostAnswer postAnswer) {
        jpaPostAnswerRepository.delete(postAnswer);

    }

    @Override
    public void deleteById(Long id) {
        jpaPostAnswerRepository.deleteById(id);
    }

    @Override
    public Optional<PostAnswer> findByPostAndTodayQuestion(Post post, TodayQuestion todayQuestion) {
        return jpaPostAnswerRepository.findPostAnswerByPostAndTodayQuestion(post , todayQuestion );
    }


}
