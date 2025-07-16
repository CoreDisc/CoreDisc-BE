package com.coredisc.domain.postAnswer;

import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;

import java.util.Optional;

public interface PostAnswerRepository {

    PostAnswer save(PostAnswer postAnswer);
    Optional<PostAnswer> findById(Long id);
    void delete(PostAnswer postAnswer);
    void deleteById(Long id);

    Optional<PostAnswer> findByPostAndTodayQuestion(Post post, TodayQuestion todayQuestion);


}
