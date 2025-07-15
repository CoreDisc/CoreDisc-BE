package com.coredisc.infrastructure.repository.postAnswerImage.queryDsl;

import com.coredisc.domain.QPost;
import com.coredisc.domain.QPostAnswer;
import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.postAnswerImage.PostAnswerImage;
import com.coredisc.domain.postAnswerImage.QPostAnswerImage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryPostAnswerImageRepositoryImpl implements QueryPostAnswerImageRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostAnswerImage> findImageAnswersByMember(Member member, Long cursorId, Pageable pageable) {

        QPostAnswerImage pai = QPostAnswerImage.postAnswerImage;
        QPostAnswer pa = QPostAnswer.postAnswer;
        QPost p = QPost.post;

        return jpaQueryFactory
                .selectFrom(pai)
                .join(pai.postAnswer, pa)
                .join(pa.post, p)
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        pa.type.eq(AnswerType.IMAGE),
                        cursorId != null ? pai.id.lt(cursorId) : null
                )
                .orderBy(pai.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public boolean existsByMemberAndIdLessThan(Member member, Long id) {

        QPostAnswerImage pai = QPostAnswerImage.postAnswerImage;
        QPostAnswer pa = QPostAnswer.postAnswer;
        QPost p = QPost.post;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(pai)
                .join(pai.postAnswer, pa)
                .join(pa.post, p)
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        pa.type.eq(AnswerType.IMAGE),
                        pai.id.lt(id)
                )
                .fetchFirst();

        return fetchOne != null;
    }
}
