package com.coredisc.application.service.member;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.common.exception.handler.MyHomeHandler;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.disc.DiscRepository;
import com.coredisc.domain.monthlyReport.MonthlyReportRepository;
import com.coredisc.domain.post.PostAnswerImage;
import com.coredisc.domain.postAnswerImage.PostAnswerImageRepository;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.domain.profileImg.ProfileImgRepository;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final ProfileImgRepository profileImgRepository;
    private final DiscRepository discRepository;
    private final PostAnswerImageRepository postAnswerImageRepository;


    @Override
    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public MemberResponseDTO.MyHomeInfoDTO getMyHomeInfo(Member member) {

        // 사용자의 팔로워 수
        Long followerCount = followRepository.countByFollowingId(member.getId());

        // 사용자의 팔로잉 수
        Long followingCount = followRepository.countByFollowerId(member.getId());

        // 총 디스크 수(월별 리포트 수)
        Long discCount = discRepository.countByMember(member);

        // 사용자의 프로필 이미지
        ProfileImg profileImg = profileImgRepository.findByMember(member);

        return MemberConverter.toMyHomeInfoDTO(member, followerCount, followingCount, discCount, profileImg);
    }

    @Override
    public MemberResponseDTO.UserHomeInfoDTO getUserHomeInfo(Member member, String targetUsername) {

        // targetUsername이 로그인한 사용자 본인의 username일 때 예외 처리
        if(member.getUsername().equals(targetUsername)) {
            throw new MyHomeHandler(ErrorStatus.SELF_PROFILE_REQUEST);
        }

        // 타사용자
        Member targetMember = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 타사용자의 팔로워 수
        Long followerCount = followRepository.countByFollowingId(targetMember.getId());

        // 타사용자의 팔로잉 수
        Long followingCount = followRepository.countByFollowerId(targetMember.getId());

        // 총 디스크 수(월별 리포트 수)
        Long discCount = discRepository.countByMember(targetMember);

        // 타사용자의 프로필 이미지
        ProfileImg profileImg = profileImgRepository.findByMember(targetMember);

        // 팔로우 여부
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(member, targetMember);

        return MemberConverter.toUserHomeInfoDTO(targetMember, followerCount, followingCount, discCount, profileImg, isFollowing);
    }

    @Override
    public CursorDTO<MemberResponseDTO.MyHomeImageAnswerDTO> getMyHomeImageAnswers(Member member, Long cursorId, Pageable page) {

        List<PostAnswerImage> postAnswerImages = postAnswerImageRepository.findImageAnswersByMember(member, cursorId, page);
        List<MemberResponseDTO.MyHomeImageAnswerDTO> myHomeImageAnswerDTOS = postAnswerImages.stream()
                .map(MemberConverter::toMyHomeImageAnswerDTO)
                .toList();

        Long lastIdOfList = postAnswerImages.isEmpty() ?
                null : postAnswerImages.get(postAnswerImages.size() - 1).getId();

        return new CursorDTO<>(myHomeImageAnswerDTOS, hasNext(member, lastIdOfList));
    }

    @Override
    public CursorDTO<MemberResponseDTO.UserHomeImageAnswerDTO> getUserHomeImageAnswers(Member member, String targetUsername,
                                                                                       Long cursorId, Pageable page) {
        // targetUsername이 로그인한 사용자 본인의 username일 때 예외 처리
        if(member.getUsername().equals(targetUsername)) {
            throw new MyHomeHandler(ErrorStatus.SELF_PROFILE_REQUEST);
        }

        // 타사용자
        Member targetMember = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<PostAnswerImage> postAnswerImages = postAnswerImageRepository.findImageAnswersByMember(targetMember, cursorId, page);
        List<MemberResponseDTO.UserHomeImageAnswerDTO> userHomeImageAnswerDTOS = postAnswerImages.stream()
                .map(MemberConverter::toUserHomeImageAnswerDTO)
                .toList();

        Long lastIdOfList = postAnswerImages.isEmpty() ?
                null : postAnswerImages.get(postAnswerImages.size() - 1).getId();

        return new CursorDTO<>(userHomeImageAnswerDTOS, hasNext(targetMember, lastIdOfList));
    }


    private Boolean hasNext(Member member, Long id) {

        if(id == null) { return false; }
        return postAnswerImageRepository.existsByMemberAndIdLessThan(member, id);
    }
}
