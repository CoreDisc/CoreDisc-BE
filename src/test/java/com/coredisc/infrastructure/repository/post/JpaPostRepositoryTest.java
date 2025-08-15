package com.coredisc.infrastructure.repository.post;

import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class JpaPostRepositoryTest {

    @Autowired
    private JpaPostRepository jpaPostRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    void existsByMemberAndStatusAndCreatedAtBetween_존재하면_true_반환() {
        // given
        PostStatus status = PostStatus.PUBLISHED;
        LocalDateTime now = LocalDateTime.now();

        Member member = Member.builder()
                .nickname("testMember")
                .email("aelrjaler@anveral.com")
                .build();

        Post post = Post.builder()
                .member(member)
                .status(PostStatus.PUBLISHED)
                .build();

        em.persist(member);
        jpaPostRepository.save(post);

        boolean exists = jpaPostRepository.existsByMemberAndStatusAndCreatedAtBetween(
                member, status, now.minusMinutes(1), now.plusMinutes(1));


        assertTrue(exists);
    }
}