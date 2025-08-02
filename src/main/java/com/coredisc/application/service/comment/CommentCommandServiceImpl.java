package com.coredisc.application.service.comment;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.CommentConverter;
import com.coredisc.common.exception.handler.CommentHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.common.exception.handler.PostHandler;
import com.coredisc.domain.Comment;
import com.coredisc.domain.comment.CommentRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.presentation.dto.comment.CommentRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandServiceImpl implements CommentCommandService {

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

        Comment comment = CommentConverter.toComment(request.getContent(), post, member);

        //Converter 적용
        return commentRepository.save(comment);

    }

    @Override
    public Comment createReply(Long commentId, CommentRequestDTO request, Long memberId) {


        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 깊이가 1을 넘을 경우 Error 반환
        if (parentComment.getDepth() >= 1) {
            throw new CommentHandler(ErrorStatus.COMMENT_DEPTH_EXCEEDED);
        }

        Comment reply = CommentConverter.toComment(request.getContent(),parentComment.getPost(),member);

        parentComment.addReply(reply);

        return commentRepository.save(reply);

    }

    @Override
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentHandler(ErrorStatus.COMMENT_NOT_FOUND));

        // 작성자 권한 확인
        if (!comment.isOwner(memberId)) {
            throw new CommentHandler(ErrorStatus.COMMENT_ACCESS_DENIED);
        }

        // 부모댓글인 경우 HardDelete 로 구현
        commentRepository.delete(comment);

    }

}
