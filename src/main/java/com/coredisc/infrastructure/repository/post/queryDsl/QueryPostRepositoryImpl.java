package com.coredisc.infrastructure.repository.post.queryDsl;

import com.coredisc.domain.QPostAnswer;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.QPost;
import com.coredisc.domain.postAnswerImage.QPostAnswerImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryPostRepositoryImpl implements  QueryPostRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findMyPostsWithAnswers(Member member, Long cursorId, Pageable pageable) {

        QPost p = QPost.post;
        QPostAnswer pa = QPostAnswer.postAnswer;
        QPostAnswerImage pai = QPostAnswerImage.postAnswerImage;

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
    public boolean existsByMemberAndIdLessThan(Member member, Long id) {

        QPost p = QPost.post;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(p)
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        p.id.lt(id)
                )
                .fetchFirst();

        return fetchOne != null;
    }
}
