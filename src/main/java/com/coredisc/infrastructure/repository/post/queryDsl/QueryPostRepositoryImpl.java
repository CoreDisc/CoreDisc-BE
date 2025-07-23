package com.coredisc.infrastructure.repository.post.queryDsl;

import com.coredisc.domain.QTodayQuestion;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.common.enums.PublicityType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.coredisc.domain.QTodayQuestion.*;
import static com.coredisc.domain.post.QPost.*;
import static com.coredisc.domain.post.QPostAnswer.*;
import static com.coredisc.domain.post.QPostAnswerImage.*;

@Repository
@RequiredArgsConstructor
public class QueryPostRepositoryImpl implements QueryPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findMyPostsWithAnswers(Member member, Long cursorId, Pageable pageable) {

        QPost p = post;
        QPostAnswer pa = postAnswer;
        QPostAnswerImage pai = postAnswerImage;

        return jpaQueryFactory
                .selectFrom(p)
                .leftJoin(p.answers, pa).fetchJoin()
                .leftJoin(pa.postAnswerImage, pai).fetchJoin()
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        cursorId != null ? p.id.lt(cursorId) : null
                )
                .orderBy(p.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Post> findUserPostsWithAnswers(Member member, boolean isCircle, Long cursorId, Pageable pageable) {

        QPost p = post;
        QPostAnswer pa = postAnswer;
        QPostAnswerImage pai = postAnswerImage;

        return jpaQueryFactory
                .selectFrom(p)
                .leftJoin(p.answers, pa).fetchJoin()
                .leftJoin(pa.postAnswerImage, pai).fetchJoin()
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        (isCircle ?
                                (p.publicity.eq(PublicityType.CIRCLE).or(p.publicity.eq(PublicityType.OFFICIAL)))
                                : p.publicity.eq(PublicityType.OFFICIAL)
                        ),
                        cursorId != null ? p.id.lt(cursorId) : null
                )
                .orderBy(p.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public boolean existsByMemberAndIdLessThan(Member member, Long id,
                                               Set<PublicityType> allowTypes) {
        QPost p = post;
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(p)
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        allowTypes != null && !allowTypes.isEmpty()
                                ? p.publicity.in(allowTypes) : null,
                        p.id.lt(id)
                )
                .fetchFirst();
        return fetchOne != null;
    }

    @Override
    public List<Post> findTempPostByMemberAndDate(Member member, LocalDate selectedDate) {
        LocalDateTime start = selectedDate.atStartOfDay();
        LocalDateTime end = selectedDate.plusDays(1).atStartOfDay();

        return jpaQueryFactory
                .selectFrom(post)
                .where(
                        post.member.eq(member),
                        post.status.eq(PostStatus.TEMP),
                        post.createdAt.goe(start),
                        post.createdAt.lt(end)
                ).orderBy(post.updatedAt.desc())
                .fetch();
    }

    @Override
    public List<PostAnswer> findTempPostAnswerByPostId(Long postId) {

        return jpaQueryFactory
                .selectFrom(postAnswer)
                .join(postAnswer.todayQuestion , todayQuestion).fetchJoin()
                .where(postAnswer.post.id.eq(postId))
                .orderBy(todayQuestion.id.asc())
                .fetch();
    }
}
