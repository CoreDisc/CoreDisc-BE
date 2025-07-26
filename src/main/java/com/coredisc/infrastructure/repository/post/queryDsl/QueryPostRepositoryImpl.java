package com.coredisc.infrastructure.repository.post.queryDsl;

import com.coredisc.common.converter.PostConverter;
import com.coredisc.domain.common.enums.FeedType;
import com.coredisc.common.util.DateUtil;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.common.enums.PublicityType;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.*;
import com.coredisc.domain.todayQuestion.QTodayQuestion;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import com.querydsl.core.BooleanBuilder;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.post.QPost;
import com.coredisc.domain.post.QPostAnswer;
import com.coredisc.domain.post.QPostAnswerImage;
import com.coredisc.presentation.dto.calendar.CalendarPostDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.coredisc.domain.follow.QFollow.follow;
import static com.coredisc.domain.member.QMember.*;
import static com.coredisc.domain.post.QPost.*;
import static com.coredisc.domain.post.QPostAnswer.*;
import static com.coredisc.domain.post.QPostAnswerImage.*;
import static com.coredisc.domain.profileImg.QProfileImg.*;
import static com.coredisc.domain.todayQuestion.QTodayQuestion.*;

@Repository
@RequiredArgsConstructor
public class QueryPostRepositoryImpl implements QueryPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findMyPostsWithAnswers(Member member, Long cursorId, Pageable pageable) {

        QPost p = QPost.post;
        QPostAnswer pa = QPostAnswer.postAnswer;
        QPostAnswerImage pai = QPostAnswerImage.postAnswerImage;

        return jpaQueryFactory
                .selectFrom(p)
                .leftJoin(p.answers, pa).fetchJoin()
                .leftJoin(pa.postAnswerImage, pai).fetchJoin()
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        cursorId != null ? p.id.lt(cursorId) : null
                )
                .orderBy(p.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Post> findUserPostsWithAnswers(Member member, boolean isCircle, Long cursorId, Pageable pageable) {

        QPost p = QPost.post;
        QPostAnswer pa = QPostAnswer.postAnswer;
        QPostAnswerImage pai = QPostAnswerImage.postAnswerImage;

        return jpaQueryFactory
                .selectFrom(p)
                .leftJoin(p.answers, pa).fetchJoin()
                .leftJoin(pa.postAnswerImage, pai).fetchJoin()
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        (isCircle ?
                                (p.publicity.eq(PublicityType.CIRCLE).or(p.publicity.eq(PublicityType.OFFICIAL)))
                                : p.publicity.eq(PublicityType.OFFICIAL)
                        ),
                        cursorId != null ? p.id.lt(cursorId) : null
                )
                .orderBy(p.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public boolean existsByMemberAndIdLessThan(Member member, Long id,
                                               Set<PublicityType> allowTypes) {
        QPost p = QPost.post;
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(p)
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        allowTypes != null && !allowTypes.isEmpty()
                                ? p.publicity.in(allowTypes) : null,
                        p.id.lt(id)
                )
                .fetchFirst();
        return fetchOne != null;
    }

    @Override
    public List<Post> findTempPostByMemberAndDate(Member member, LocalDate selectedDate) {
        LocalDateTime start = selectedDate.atStartOfDay();
        LocalDateTime end = selectedDate.plusDays(1).atStartOfDay();

        return jpaQueryFactory
                .selectFrom(post)
                .where(
                        post.member.eq(member),
                        post.status.eq(PostStatus.TEMP),
                        post.createdAt.goe(start),
                        post.createdAt.lt(end)
                ).orderBy(post.updatedAt.desc())
                .fetch();
    }

    @Override
    public List<PostAnswer> findTempPostAnswerByPostId(Long postId) {

        return jpaQueryFactory
                .selectFrom(postAnswer)
                .join(postAnswer.todayQuestion, todayQuestion).fetchJoin()
                .where(postAnswer.post.id.eq(postId))
                .orderBy(todayQuestion.id.asc())
                .fetch();
    }

    @Override
    public List<PostResponseDTO.PostFeedResponseDTO.PostSummary> findPostFeed(Long memberId, FeedType feedType, Long lastPostId, Integer size) {

        BooleanBuilder condition = new BooleanBuilder();

        // 기본 조건: 발행된 게시글만
        condition.and(post.status.eq(PostStatus.PUBLISHED));

        // 피드 타입별 필터링
        if (feedType == FeedType.ALL) {
            // 팔로우하는 모든 사용자의 게시글
            condition.and(post.member.id.in(
                    jpaQueryFactory
                            .select(follow.following.id)
                            .from(follow)
                            .where(follow.follower.id.eq(memberId))
            ));
        } else if (feedType == FeedType.CORE) {
            // 친한친구로 설정한 사용자들의 게시글
            condition.and(post.member.id.in(
                    jpaQueryFactory
                            .select(follow.following.id)
                            .from(follow)
                            .where(follow.follower.id.eq(memberId)
                                    .and(follow.isCircle.eq(true)))
            ));
        }

        // 공개 범위 필터링
        BooleanBuilder visibilityCondition = new BooleanBuilder();

        // PUBLIC 게시글은 누구나 볼 수 있음
        visibilityCondition.or(post.publicity.eq(PublicityType.OFFICIAL));

        // CIRCLE 게시글은 서로 친한친구인 경우만 보이도록
        visibilityCondition.or(
                post.publicity.eq(PublicityType.CIRCLE)
                        .and(post.member.id.in(
                                jpaQueryFactory
                                        .select(follow.following.id)
                                        .from(follow)
                                        .where(follow.follower.id.eq(memberId)
                                                .and(follow.isCircle.eq(true)))
                        ))

        );

        condition.and(visibilityCondition);

        // 커서 페이지네이션 조건 추가
        if (lastPostId != null) {
            condition.and(post.id.lt(lastPostId));
        }


        // Pull 모델: 실시간으로 게시글 조회
        List<Post> posts = jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.member, member).fetchJoin()
                .leftJoin(member.profileImg, profileImg).fetchJoin()
                .where(condition)
                .orderBy(post.id.desc()) // 최신순 정렬
                .limit(size + 1) // hasNext 체크를 위해 +1
                .fetch();

        // 게시글 ID 리스트 추출
        List<Long> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        //  모든 게시글의 답변을 한 번에 조회 (N+1 방지)
        List<com.coredisc.domain.post.PostAnswer> allAnswers = jpaQueryFactory
                .selectFrom(postAnswer)
                .leftJoin(postAnswer.postAnswerImage, postAnswerImage).fetchJoin()
                .leftJoin(postAnswer.todayQuestion).fetchJoin()
                .where(postAnswer.post.id.in(postIds))
                .orderBy(postAnswer.post.id.asc(), postAnswer.id.asc())
                .fetch();

        // 게시글별로 답변 그룹핑
        Map<Long, List<PostAnswer>> answersMap = allAnswers.stream()
                .collect(Collectors.groupingBy(answer -> answer.getPost().getId()));

        // PostSummary DTO로 변환
        return posts.stream()
                .map(postEntity -> {
                    List<com.coredisc.domain.post.PostAnswer> postAnswers =
                            answersMap.getOrDefault(postEntity.getId(), List.of());
                    return PostConverter.toPostSummary(postEntity, postAnswers);
                })
                .toList();
    }

    @Override
    public Post findPostDetail(Long memberId, Long postId) {
        // 엔티티 조회로 한 번에 조회하고 Converter 로 변환하기

        return  jpaQueryFactory
                .selectFrom(post)
                .leftJoin(post.answers, postAnswer).fetchJoin()
                .leftJoin(postAnswer.postAnswerImage, postAnswerImage).fetchJoin()
                .leftJoin(post.member, member).fetchJoin()
                .leftJoin(member.profileImg, profileImg).fetchJoin()
                .where(post.id.eq(postId)
                        .and(post.status.eq(PostStatus.PUBLISHED)))
                .fetchOne();
    }






    // 캘린더 기능에 사용하기 위한 메소드 추가
    @Override
    public List<CalendarPostDTO> findPostInfoByMemberAndMonth(int year, int month, Member member) {
        QPost p = QPost.post;

        LocalDate start = DateUtil.getStartDate(year, month);
        LocalDate end = DateUtil.getEndDate(year, month);

        return jpaQueryFactory
                .select(Projections.constructor(
                        CalendarPostDTO.class,
                        p.id,
                        p.createdAt
                ))
                .from(p)
                .where(
                        p.member.eq(member),
                        p.status.ne(PostStatus.TEMP),
                        p.createdAt.between(start.atStartOfDay(), end.atTime(LocalTime.MAX))
                )
                .orderBy(p.createdAt.asc())
                .fetch();
    }

    @Override
    public List<Post> findPostsByCreatedDate(LocalDate targetDate) {
        QPost post = QPost.post;

        return jpaQueryFactory
                .selectFrom(post)
                .where(post.createdAt.between(
                        targetDate.atStartOfDay(),
                        targetDate.plusDays(1).atStartOfDay().minusNanos(1))
                )
                .fetch();
    }

    @Override
    public List<Post> findPostsByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        QPost post = QPost.post;

        return jpaQueryFactory
                .selectFrom(post)
                .where(post.createdAt.between(start, end))
                .fetch();
    }

    @Override
    public List<Post> findFirstPostPerMemberInMonth(LocalDateTime start, LocalDateTime end) {
        QPost post = QPost.post;

        return jpaQueryFactory
                .selectFrom(post)
                .where(post.createdAt.between(start, end))
                .where(
                        post.createdAt.eq(
                                JPAExpressions
                                        .select(post.createdAt.min())
                                        .from(post)
                                        .where(post.member.id.eq(post.member.id)) // 같은 멤버
                                        .where(post.createdAt.between(start, end))
                        )
                )
                .fetch();
    }

    @Override
    public List<PostAnswer> findByCreatedAtBetweenAndTodayQuestionId(LocalDateTime start, LocalDateTime end, Long todayQuestionId) {
        QPostAnswer postAnswer = QPostAnswer.postAnswer;

        return jpaQueryFactory.selectFrom(postAnswer)
                .where(postAnswer.createdAt.between(start, end)
                        .and(postAnswer.todayQuestion.id.eq(todayQuestionId)))
                .fetch();
    }
}
