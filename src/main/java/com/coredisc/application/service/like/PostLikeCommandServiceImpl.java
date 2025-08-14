package com.coredisc.application.service.like;

import com.coredisc.application.service.fcm.FcmService;
import com.coredisc.application.service.notification.NotificationCommandService;
import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.PostConverter;
import com.coredisc.common.exception.handler.LikeHandler;
import com.coredisc.common.exception.handler.PostHandler;
import com.coredisc.domain.common.enums.NotificationType;
import com.coredisc.domain.device.Device;
import com.coredisc.domain.device.DeviceRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostLike;
import com.coredisc.domain.post.PostLikeRepository;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.presentation.dto.notification.NotificationRequestDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeCommandServiceImpl implements PostLikeCommandService{


    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final DeviceRepository deviceRepository;
    private final NotificationCommandService notificationCommandService;
    private final FcmService fcmService;

    @Transactional
    public PostResponseDTO.PostLikeDto createLike(Long postId, Member member) {
        // post 존재 여부 확인
        Post post = findAndValidatePost(postId);
        // 1차 중복 요청 방지 - LikeHandler
        if(postLikeRepository.existsByMemberAndPost(member,post)) {
            throw new LikeHandler(ErrorStatus.POST_LIKE_DUPLICATED);

        }
        PostLike postLike = PostLike.create(post,member);

        // unique 제약을 걸어 중복 방지한다. - 동시 요청 방지
        PostLike savedPostlike;
        try {
            savedPostlike = postLikeRepository.createPostLike(postLike);
        } catch (DataIntegrityViolationException e) {
            throw new LikeHandler(ErrorStatus.POST_LIKE_DUPLICATED);
        }

        // 자신의 게시글에 좋아요를 누를 시에는 알림이 생성되지 않도록
        if ( !post.getMember().equals(member)) {
            notificationCommandService.createNotification(
                    new NotificationRequestDTO(
                            NotificationType.LIKE, // 알림 타입 (좋아요)
                            member, //sender
                            post.getMember(), // receiver
                            member.getNickname()+"님이 게시글에 마음을 남겼어요.",
                            post.getId() // 클릭 시 게시글로 이동
                    )
            );
        }

        List<Device> devices = deviceRepository.findByMemberAndIsActiveTrue(post.getMember());

        // 푸시 알림 내용 설정
        String title = "CoreDisc";
        String body = member.getNickname() + "님이 게시글에 마음을 남겼어요.";

        // 푸시 알림에 보낼 데이터 설정 (알림 타입 및 알림 클릭 -> 이동할 targetId)
        Map<String, String> data = new HashMap<>();
        data.put("notificationType", NotificationType.LIKE.name());
        data.put("targetId", String.valueOf(post.getId()));

        for (Device device : devices) {
            String token = device.getToken();
            if (fcmService.isTokenValid(token)) {
                fcmService.sendNotificationToToken(token, title, body, data);
                log.info("좋아요 알림 발송됨: memberId={}, token={}", post.getMember().getId(), token);
            } else {
                log.warn("좋아요 알림 발송 안됨: 유효하지 않은 토큰 발견. memberId={}, token={}", post.getMember().getId(), token);
            }
        }

        return PostConverter.toPostLikeDto(savedPostlike.getPost().getId(),true);
    }

    @Transactional
    public PostResponseDTO.PostLikeDto deleteLike(Long postId, Member member) {
        Post post = findAndValidatePost(postId);

        postLikeRepository.deleteByPostAndMember(post,member);

        return PostConverter.toPostLikeDto(postId,false);
    }


    private Post findAndValidatePost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() ->new PostHandler(ErrorStatus.POST_NOT_FOUND) );
    }
}
