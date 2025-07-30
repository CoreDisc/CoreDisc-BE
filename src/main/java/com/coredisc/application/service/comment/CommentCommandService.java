package com.coredisc.application.service.comment;

import com.coredisc.domain.Comment;
import com.coredisc.presentation.dto.comment.CommentRequestDTO;

public interface CommentCommandService {

    public Comment createComment(Long postId, CommentRequestDTO request, Long memberId);
}
