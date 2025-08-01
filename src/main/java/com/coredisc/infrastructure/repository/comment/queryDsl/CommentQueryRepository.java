package com.coredisc.infrastructure.repository.comment.queryDsl;


import com.coredisc.domain.Comment;
import com.coredisc.presentation.dto.comment.CommentResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;

import java.util.List;

public interface CommentQueryRepository {

    CursorDTO<Comment> findParentCommentsByCursor(Long postId, Long cursorId, Integer size, Long memberId);

    CursorDTO<Comment> findRepliesByParentIds(Long parentId, Long cursorId, Integer size, Long memberId);

}
