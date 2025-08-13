package com.coredisc.application.service.comment;

import com.coredisc.application.service.fcm.FcmService;
import com.coredisc.application.service.notification.NotificationCommandService;
import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.CommentConverter;
import com.coredisc.common.exception.handler.CommentHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.common.exception.handler.PostHandler;
import com.coredisc.domain.Comment;
import com.coredisc.domain.comment.CommentRepository;
import com.coredisc.domain.common.enums.NotificationType;
import com.coredisc.domain.device.Device;
import com.coredisc.domain.device.DeviceRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.presentation.dto.comment.CommentRequestDTO;
import com.coredisc.presentation.dto.notification.NotificationRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandServiceImpl implements CommentCommandService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationCommandService notificationCommandService;
    private final FcmService fcmService;

    public Comment createComment(Long postId, CommentRequestDTO request, Long memberId) {
        // 게시글 존재 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        // 멤버 존재 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Comment comment = CommentConverter.toComment(request.getContent(), post, member);

        notificationCommandService.createNotification(
                new NotificationRequestDTO(
                        NotificationType.COMMENT, // 알림 타입 (댓글)
                        member, // sender
                        post.getMember(), // receiver
                        member.getNickname()+"님이 게시글에 댓글을 남겼어요.",
                        post.getId() // 클릭 시 게시글로 이동
                )
        );

        List<Device> devices = deviceRepository.findByMemberAndIsActiveTrue(post.getMember());

        // 푸시 알림 내용 설정
        String title = "CoreDisc";
        String body = member.getNickname()+"님이 게시글에 댓글을 남겼어요.";

        for (Device device : devices) {
            String token = device.getToken();
            if (fcmService.isTokenValid(token)) {
                fcmService.sendNotificationToToken(token, title, body);
                log.info("댓글 알림 발송됨: memberId={}, token={}", post.getMember().getId(), token);
            } else {
                log.warn("댓글 알림 발송 안됨: 유효하지 않은 토큰 발견. memberId={}, token={}", post.getMember().getId(), token);
            }
        }

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

        notificationCommandService.createNotification(
                new NotificationRequestDTO(
                        NotificationType.COMMENT_REPLY, // 알림 타입 (대댓글)
                        member, // sender
                        parentComment.getMember(), // receiver
                        member.getNickname()+"님이 게시글에 댓글을 남겼어요.",
                        parentComment.getPost().getId() // 클릭 시 댓글 달렸던 게시글로 이동
                )
        );

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
