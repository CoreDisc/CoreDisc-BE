package com.coredisc.application.service.comment;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.comment.CommentResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;

public interface CommentQueryService {
    CursorDTO<CommentResponseDTO.CommentCreateResponse> getChildComments(Long parentId, Long cursorId, Integer size, Member member);

    CursorDTO<CommentResponseDTO.CommentCreateResponse> getParentComments(Long postId, Long cursorId, Integer size, Member member);

}
