package com.coredisc.domain.post;

import com.coredisc.domain.TodayQuestion;

import java.util.List;
import java.util.Optional;

public interface PostAnswerRepository {

    PostAnswer save(PostAnswer postAnswer);
    Optional<PostAnswer> findById(Long id);
    void delete(PostAnswer postAnswer);
    void deleteById(Long id);

    Optional<PostAnswer> findByPostAndTodayQuestion(Post post, TodayQuestion todayQuestion);


}
