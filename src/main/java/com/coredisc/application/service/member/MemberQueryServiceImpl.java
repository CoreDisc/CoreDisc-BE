package com.coredisc.application.service.member;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.common.exception.handler.MyHomeHandler;
import com.coredisc.common.util.DateUtil;
import com.coredisc.common.util.FormatNumberUtil;
import com.coredisc.domain.block.BlockRepository;
import com.coredisc.domain.common.enums.AnswerType;
import com.coredisc.domain.common.enums.PublicityType;
import com.coredisc.domain.disc.DiscRepository;
import com.coredisc.domain.follow.FollowRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.PostAnswer;
import com.coredisc.domain.post.PostAnswerImage;
import com.coredisc.domain.post.PostRepository;
import com.coredisc.domain.profileImg.ProfileImg;
import com.coredisc.domain.profileImg.ProfileImgRepository;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberQueryServiceImpl implements MemberQueryService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final ProfileImgRepository profileImgRepository;
    private final PostRepository postRepository;
    private final BlockRepository blockRepository;
    private final DiscRepository discRepository;


    @Override
    public Member getMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new AuthHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public MemberResponseDTO.MyHomeInfoDTO getMyHomeInfo(Member member) {

        // 사용자의 팔로워 수
        String followerCount = FormatNumberUtil.formatNumberUnit(
                followRepository.countByFollowingId(member.getId())
        );

        // 사용자의 팔로잉 수
        String followingCount = FormatNumberUtil.formatNumberUnit(
                followRepository.countByFollowerId(member.getId())
        );

        // 총 디스크 수(월별 리포트 수)
        String discCount = FormatNumberUtil.formatNumberUnit(
                discRepository.countByMember(member)
        );

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
        String followerCount = FormatNumberUtil.formatNumberUnit(
                followRepository.countByFollowingId(targetMember.getId())
        );

        // 타사용자의 팔로잉 수
        String followingCount = FormatNumberUtil.formatNumberUnit(
                followRepository.countByFollowerId(targetMember.getId())
        );

        // 총 디스크 수(월별 리포트 수)
        String discCount = FormatNumberUtil.formatNumberUnit(
                discRepository.countByMember(targetMember)
        );


        // 타사용자의 프로필 이미지
        ProfileImg profileImg = profileImgRepository.findByMember(targetMember);

        // 팔로우 여부
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(member, targetMember);

        // 차단 여부
        boolean isBlocked = blockRepository.existsByBlockerAndBlocked(member, targetMember);

        return MemberConverter.toUserHomeInfoDTO(
                targetMember, followerCount, followingCount,
                discCount, profileImg, isFollowing, isBlocked
        );
    }

    @Override
    public CursorDTO<MemberResponseDTO.MyHomePostDTO> getMyHomePosts(Member member, Long cursorId, Pageable page) {

        List<Post> posts = postRepository.findMyPostsWithAnswers(member, cursorId, page);

        List<MemberResponseDTO.MyHomePostDTO> result = posts.stream()
                .map(p -> toHomePostDTO(
                        p,
                        (post, imgDto) -> MemberConverter.toMyHomePostDTO(post, imgDto, null),
                        (post, txtDto) -> MemberConverter.toMyHomePostDTO(post, null, txtDto)
                ))
                .toList();

        Set<PublicityType> allowTypes = Set.of(PublicityType.OFFICIAL, PublicityType.CIRCLE, PublicityType.PERSONAL);
        Long lastId = posts.isEmpty() ? null : posts.get(posts.size() - 1).getId();
        return new CursorDTO<>(result, hasNext(member, lastId, allowTypes));
    }


    @Override
    public CursorDTO<MemberResponseDTO.UserHomePostDTO> getUserHomePosts(Member member, String targetUsername, Long cursorId, Pageable page) {

        // targetUsername이 로그인한 사용자 본인의 username일 때 예외 처리
        if(member.getUsername().equals(targetUsername)) {
            throw new MyHomeHandler(ErrorStatus.SELF_PROFILE_REQUEST);
        }

        // 존재하지 않는 username일 때
        Member targetMember = memberRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 차단된 사용자일 때
        if(blockRepository.existsByBlockerAndBlocked(member, targetMember)) {
            throw new MyHomeHandler(ErrorStatus.BLOCKED_MEMBER_REQUEST);
        }

        boolean isCircle = followRepository.existsByFollowerAndFollowingAndIsCircle(targetMember, member, true);

        List<Post> posts = postRepository.findUserPostsWithAnswers(targetMember, isCircle, cursorId, page);

        List<MemberResponseDTO.UserHomePostDTO> result = posts.stream()
                .map(p -> toHomePostDTO(
                        p,
                        (post, imgDto) -> MemberConverter.toUserHomePostDTO(post, imgDto, null),
                        (post, txtDto) -> MemberConverter.toUserHomePostDTO(post, null, txtDto)
                ))
                .toList();

        Set<PublicityType> allowTypes = isCircle
                ? Set.of(PublicityType.OFFICIAL, PublicityType.CIRCLE)
                : Set.of(PublicityType.OFFICIAL);
        Long lastId = posts.isEmpty() ? null : posts.get(posts.size() - 1).getId();
        return new CursorDTO<>(result, hasNext(targetMember, lastId, allowTypes));
    }


    private Boolean hasNext(Member member, Long id, Set<PublicityType> allowTypes) {

        if(id == null) { return false; }
        return postRepository.existsByMemberAndIdLessThan(member, id, allowTypes);
    }

    private <T> T toHomePostDTO(Post post,
                                java.util.function.BiFunction<Post, MemberResponseDTO.PostImageThumbnailDTO, T> imageDtoBuilder,
                                java.util.function.BiFunction<Post, MemberResponseDTO.PostTextThumbnailDTO, T> textDtoBuilder) {

        List<PostAnswer> answers = post.getAnswers();
        Optional<PostAnswer> imageAnswerOpt = answers.stream()
                .filter(a -> a.getType() == AnswerType.IMAGE && a.getPostAnswerImage().getThumbnailUrl() != null)
                .findFirst();

        if (imageAnswerOpt.isPresent()) {
            PostAnswerImage pai = imageAnswerOpt.get().getPostAnswerImage();

            MemberResponseDTO.PostImageThumbnailDTO imageDto = MemberConverter.toPostImageThumbnailDTO(pai);
            return imageDtoBuilder.apply(post, imageDto);
        } else {
            String weekday = DateUtil.getWeekdayShort(post.getCreatedAt());
            String createdDate = DateUtil.getYYMMDD(post.getCreatedAt());
            // TODO: 카테고리 조회 추가

            MemberResponseDTO.PostTextThumbnailDTO textDto = MemberConverter.toPostTextThumbnailDTO(weekday, createdDate);
            return textDtoBuilder.apply(post, textDto);
        }
    }

}
