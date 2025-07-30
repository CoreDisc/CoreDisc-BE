package com.coredisc.application.service.comment;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.CommentConverter;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.common.exception.handler.PostHandler;
import com.coredisc.domain.Comment;
import com.coredisc.domain.comment.CommentRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.presentation.dto.comment.CommentRequestDTO;
import com.coredisc.presentation.dto.comment.CommentResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandServiceImpl implements  CommentCommandService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Comment createComment(Long postId, CommentRequestDTO request, Long memberId) {
        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Comment comment = CommentConverter.toComment(request.getContent(),post,member);

        //Converter 적용
        return commentRepository.save(comment);

    }

}
