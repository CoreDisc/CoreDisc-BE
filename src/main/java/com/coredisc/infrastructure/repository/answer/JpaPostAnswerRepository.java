package com.coredisc.infrastructure.repository.answer;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaPostAnswerRepository extends JpaRepository<PostAnswer,Long> {

    Optional<PostAnswer> findPostAnswerByPostAndAnswerOrderAndCreatedAtBetween(Post post, Integer answerOrder, LocalDateTime createdAtAfter, LocalDateTime createdAtBefore);

    boolean existsByPostMemberAndAnswerOrderAndPostCreatedAtBetween(
            Member member,
            int answerOrder,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
}