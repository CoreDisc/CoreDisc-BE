package com.coredisc.infrastructure.repository.answer;

import com.coredisc.domain.todayQuestion.TodayQuestion;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaPostAnswerRepository extends JpaRepository<PostAnswer,Long> {

    Optional<PostAnswer> findPostAnswerByPostAndAnswerOrderAndCreatedAtBetween(Post post, Integer answerOrder, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);

    Optional<PostAnswer> findPostAnswerByPostAndTodayQuestion(Post post , TodayQuestion todayQuestion);

    List<PostAnswer> findByCreatedAtBetweenAndTodayQuestionId(LocalDateTime start, LocalDateTime end, Long todayQuestionId);

}
