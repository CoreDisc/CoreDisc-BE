package com.coredisc.application.service.member;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.converter.MemberConverter;
import com.coredisc.common.converter.SearchHistoryConverter;
import com.coredisc.common.exception.handler.AuthHandler;
import com.coredisc.common.exception.handler.MemberHandler;
import com.coredisc.common.exception.handler.MyHomeHandler;
import com.coredisc.common.util.FormatNumberUtil;
import com.coredisc.domain.block.BlockRepository;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.common.enums.PublicityType;
import com.coredisc.domain.common.enums.SearchType;
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
import com.coredisc.domain.searchHistory.SearchHistory;
import com.coredisc.domain.searchHistory.SearchHistoryRepository;
import com.coredisc.presentation.dto.cursor.CursorDTO;
import com.coredisc.presentation.dto.member.MemberResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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
    private final SearchHistoryRepository searchHistoryRepository;


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

        // 총 게시글 수 (PUBLISHED인 것만)
        String postCount = FormatNumberUtil.formatNumberUnit(
                postRepository.countByMemberAndStatus(member, PostStatus.PUBLISHED)
        );

        // 사용자의 프로필 이미지
        ProfileImg profileImg = profileImgRepository.findByMember(member);

        return MemberConverter.toMyHomeInfoDTO(member, followerCount, followingCount, postCount, profileImg);
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

        // 타사용자의 프로필 이미지
        ProfileImg profileImg = profileImgRepository.findByMember(targetMember);

        // 팔로우 여부
        boolean isFollowing = followRepository.existsByFollowerAndFollowing(member, targetMember);

        // 차단 여부
        boolean isBlocked = blockRepository.existsByBlockerAndBlocked(member, targetMember);

        // Member가 targetMember의 Circle인지 여부
        boolean isCircle = followRepository.existsByFollowerAndFollowingAndIsCircle(targetMember, member, true);

        List<PublicityType> visibleTypes;
        if (!isCircle) {
            // Circle이 아니면 총 게시글 수 == PUBLISHED면서 OFFICIAL
            visibleTypes = List.of(PublicityType.OFFICIAL);
        } else {
            // Circle이면 총 게시글 수 == PUBLISHED면서 OFFICIAL + CIRCLE
            visibleTypes = Arrays.asList(PublicityType.OFFICIAL, PublicityType.CIRCLE);
        }

        String postCount = FormatNumberUtil.formatNumberUnit(
                postRepository.countByMemberAndStatusAndPublicityIn(targetMember, PostStatus.PUBLISHED, visibleTypes)
        );

        return MemberConverter.toUserHomeInfoDTO(
                targetMember, followerCount, followingCount,
                postCount, profileImg, isFollowing, isBlocked
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
    public CursorDTO<MemberResponseDTO.MyHomePostDTO> getUserHomePosts(Member member, String targetUsername, Long cursorId, Pageable page) {

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

        List<MemberResponseDTO.MyHomePostDTO> result = posts.stream()
                .map(p -> toHomePostDTO(
                        p,
                        (post, imgDto) -> MemberConverter.toMyHomePostDTO(post, imgDto, null),
                        (post, txtDto) -> MemberConverter.toMyHomePostDTO(post, null, txtDto)
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

        // 답변이 아예 없으면 null 반환
        if (answers.isEmpty()) return null;

        // 1. answerOrder 기준 정렬
        answers.sort(java.util.Comparator.comparing(PostAnswer::getAnswerOrder));

        // 2. 첫 번쩨 답변 가져오기
        PostAnswer first = answers.get(0);

        // 3. 첫 번째 답변 종류에 맞는 썸네일 dto 생성
        if (first.isImageAnswer() && first.getPostAnswerImage() != null) {
            PostAnswerImage pai = first.getPostAnswerImage();
            MemberResponseDTO.PostImageThumbnailDTO imageDto = MemberConverter.toPostImageThumbnailDTO(pai);
            return imageDtoBuilder.apply(post, imageDto);
        } else if (first.isTextAnswer()) {
            String content = first.getTextContent();
            MemberResponseDTO.PostTextThumbnailDTO textDto = MemberConverter.toPostTextThumbnailDTO(content);
            return textDtoBuilder.apply(post, textDto);
        } else {
            return null;
        }
    }


    // 검색 화면 사용자 검색
    @Override
    public CursorDTO<MemberResponseDTO.SearchMemberResultDTO> getMemberSearchList(Member member, String keyword, Boolean record, Long cursorId, Integer pageSize) {
        
        // 키워드 없을 시 예외처리
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new MemberHandler(ErrorStatus.INVALID_SEARCH_KEYWORD);
        }

        // 검색 기록 저장
        if (Boolean.TRUE.equals(record)) {
            SearchHistory history = searchHistoryRepository
                    .findByMemberAndKeywordAndSearchType(member, keyword, SearchType.MEMBER)
                    .map(existing -> {      // 이미 검색 되어있을 시 최근 검색으로
                        existing.updateTimestamp();
                        return existing;
                    })
                    .orElseGet(() -> SearchHistoryConverter.toSearchHistory(member, keyword, SearchType.MEMBER));

            searchHistoryRepository.save(history);
        }

        List<Member> memberList = memberRepository.findMemberListByKeyword(keyword, cursorId, pageSize);

        boolean hasNext = memberList.size() > pageSize;

        if (hasNext) {
            memberList = memberList.subList(0, pageSize);
        }

        List<MemberResponseDTO.SearchMemberResultDTO> memberSearchList = memberList.stream()
                .map(members -> MemberConverter.toSearchMemberResultDTO(members, members.getProfileImg()))
                .toList();

        return new CursorDTO<>(memberSearchList, hasNext);
    }

}
