package com.coredisc.application.service.comment;

import com.coredisc.common.converter.CommentConverter;
import com.coredisc.domain.Comment;
import com.coredisc.domain.comment.CommentRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.comment.CommentResponseDTO;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryServiceImpl implements CommentQueryService{

    private final CommentRepository commentRepository;


    @Override
    public CursorDTO<CommentResponseDTO.CommentCreateResponse> getParentComments(Long postId, Long cursorId, Integer size, Member member){

        CursorDTO<Comment> page = commentRepository.findParentCommentByCursor(postId, cursorId, size, member.getId());

        return new CursorDTO<>(page.getValues().stream()
                .map(CommentConverter::toCreateResponse)
                .toList(),page.getHasNext());
    }

    @Override
    public CursorDTO<CommentResponseDTO.CommentCreateResponse> getChildComments(Long parentId, Long cursorId, Integer size, Member member){

        CursorDTO<Comment> page = commentRepository.findRepliesByParentId(parentId, cursorId, size,member.getId());

        return new CursorDTO<>(page.getValues().stream()
                .map(CommentConverter::toCreateResponse)
                .toList(),page.getHasNext());
    }

}
