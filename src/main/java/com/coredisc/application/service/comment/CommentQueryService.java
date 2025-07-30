package com.coredisc.application.service.comment;

import com.coredisc.domain.Comment;
import com.coredisc.domain.comment.CommentRepository;
import com.coredisc.presentation.dto.comment.CommentResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

    private final CommentRepository commentRepository;

}
