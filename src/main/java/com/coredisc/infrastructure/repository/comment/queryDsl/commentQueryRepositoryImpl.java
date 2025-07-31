package com.coredisc.infrastructure.repository.comment.queryDsl;

import com.coredisc.domain.Comment;
import com.coredisc.domain.QComment;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.coredisc.domain.QComment.*;

@Repository
@RequiredArgsConstructor
public class commentQueryRepositoryImpl implements CommentQueryRepository{

    private final JPAQueryFactory queryFactory;


    @Override
    public CursorDTO<Comment> findParentCommentsByCursor(Long postId, Long cursorId, Integer size, Long memberId) {

        List<Comment> results = queryFactory
                .selectFrom(comment)
                .where(
                        comment.post.id.eq(postId),
                        comment.depth.eq(0),
                        cursorId != null ? comment.id.lt(cursorId) : null
                )
                .orderBy(comment.id.desc())
                .limit(size+1)
                .fetch();
        return buildCursorPage(results,size);
    }

    @Override
    public CursorDTO<Comment> findRepliesByParentIds(Long parentId, Long cursorId, Integer size, Long memberId) {
        List<Comment> results = queryFactory
                .selectFrom(comment)
                .where(
                        comment.parent.id.eq(parentId),
                        cursorId != null ? comment.id.lt(cursorId) : null
                )
                .orderBy(comment.id.desc())
                .limit(size+1)
                .fetch();

        return buildCursorPage(results,size);
    }

    private CursorDTO<Comment> buildCursorPage(List<Comment> results, int size){
        boolean hasNext = results.size() > size;
        if(hasNext){
            results = results.subList(0, size);
        }
        return new CursorDTO<>(results, hasNext);
    }
}