package com.coredisc.domain.comment;

import com.coredisc.domain.Comment;
import com.coredisc.presentation.dto.cursor.CursorDTO;

import java.util.Optional;

public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(Long id);

    void delete(Comment comment);

    //댓글 존재 여부 확인
    boolean existsById(Long commentId);

    // 부모 댓글만 조회
    CursorDTO<Comment> findParentCommentByCursor(Long postId, Long cursorId, Integer size, Long memberId);

    // 특정 부모 댓글의 대댓글 조회
    CursorDTO<Comment> findRepliesByParentId(Long parentId, Long cursorId, Integer size, Long memberId);

}
