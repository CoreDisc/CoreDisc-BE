package com.coredisc.application.service.member;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.common.exception.handler.MyHomeHandler;
import com.coredisc.common.util.DateUtil;
import com.coredisc.domain.PostAnswer;
import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.monthlyReport.MonthlyReportRepository;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.domain.postAnswerImage.PostAnswerImage;
import com.coredisc.domain.postAnswerImage.PostAnswerImageRepository;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.domain.profileImg.ProfileImgRepository;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final ProfileImgRepository profileImgRepository;
    private final MonthlyReportRepository monthlyReportRepository;
    private final PostAnswerImageRepository postAnswerImageRepository;
    private final PostRepository postRepository;


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
        Long discCount = monthlyReportRepository.countByMember(member);

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
        Long discCount = monthlyReportRepository.countByMember(targetMember);

        // 타사용자의 프로필 이미지
        ProfileImg profileImg = profileImgRepository.findByMember(targetMember);

        // 팔로우 여부
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(member, targetMember);

        return MemberConverter.toUserHomeInfoDTO(targetMember, followerCount, followingCount, discCount, profileImg, isFollowing);
    }

    @Override
    public CursorDTO<MemberResponseDTO.MyHomePostDTO> getMyHomePosts(Member member, Long cursorId, Pageable page) {

        // 1. Post 리스트 페이징 조회
        List<Post> posts = postRepository.findMyPostsWithAnswers(member,cursorId, page);

        List<MemberResponseDTO.MyHomePostDTO> result = new ArrayList<>();
        for (Post p : posts) {
            List<PostAnswer> answers = p.getAnswers();

            // 2. 이미지 답변 있는지 우선 검사
            Optional<PostAnswer> imageAnswerOpt = answers.stream()
                    .filter(a -> a.getType() == AnswerType.IMAGE && a.getPostAnswerImage().getThumbnailUrl() != null)
                    .findFirst();

            // 3-1. 이미지 답변이 존재하는 경우
            if(imageAnswerOpt.isPresent()) {
                PostAnswerImage pai = imageAnswerOpt.get().getPostAnswerImage();
                MemberResponseDTO.PostImageThumbnailDTO imageDto = MemberConverter.toPostImageThumbnailDTO(pai);

                result.add(MemberConverter.toMyHomePostDTO(p, imageDto, null));
            }
            else { // 3-2. 텍스트 답변만 존재하는 경우
                String weekday = DateUtil.getWeekdayShort(p.getCreatedAt());
                String createdDate = DateUtil.getYYMMDD(p.getCreatedAt());
                // TODO: 답변들의 질문 카테고리 구하기

               MemberResponseDTO.PostTextThumbnailDTO textDto = MemberConverter.toPostTextThumbnailDTO(weekday, createdDate);

               result.add(MemberConverter.toMyHomePostDTO(p, null, textDto));
            }
        }

        Long lastId = posts.isEmpty() ? null : posts.get(posts.size() - 1).getId();
        return new CursorDTO<>(result, hasNext(member, lastId));
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
        return postRepository.existsByMemberAndIdLessThan(member, id);
    }
}
