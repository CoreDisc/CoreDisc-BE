package com.coredisc.domain.post;

import com.coredisc.domain.common.enums.FeedType;
import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.common.enums.PublicityType;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.calendar.CalendarPostDTO;
import com.coredisc.presentation.dto.post.PostResponseDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository {

    Post save(Post post);
    Optional<Post> findById(Long id);
    void delete(Post post);
    void deleteById(Long id);
    long countByMemberAndStatus(Member member, PostStatus status);
    long countByMemberAndStatusAndPublicityIn(Member member, PostStatus status, List<PublicityType> publicityTypes);


    // QueryDSL
    List<Post> findMyPostsWithAnswers(Member member, Long cursorId, Pageable pageable);
    List<Post> findUserPostsWithAnswers(Member member, boolean isCircle, Long cursorId, Pageable pageable);
    boolean existsByMemberAndIdLessThan(Member member, Long id, Set<PublicityType> allowTypes);
    List<Post> findTempPostByMemberAndDate(Member member, LocalDate selectedDate);

    //단건 조회
    Post findPostDetail(Member member, Long postId);

    // 임시저장 게시글 조회
    List<PostAnswer> findTempPostWithAnswers(Long postId);

    // 게시글 동적 조회
    List<PostResponseDTO.PostFeedResponseDTO.PostSummary> findPostFeed(Member member, FeedType feedType, Long lastPostId, Integer size);


    List<CalendarPostDTO> findPostInfoByMemberAndMonth(int year, int month, Member member);
}
