package com.coredisc.infrastructure.repository.answer;

import com.coredisc.domain.TodayQuestion;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaPostAnswerRepository extends JpaRepository<PostAnswer,Long> {

    Optional<PostAnswer> findPostAnswerByPostAndTodayQuestion(Post post , TodayQuestion todayQuestion);



}
