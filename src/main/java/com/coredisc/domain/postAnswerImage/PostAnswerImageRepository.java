package com.coredisc.domain.postAnswerImage;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.PostAnswerImage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostAnswerImageRepository {

    void delete(PostAnswerImage postAnswerImage);
    // QueryDsl
    List<PostAnswerImage> findImageAnswersByMember(Member member, Long cursorId, Pageable pageable);
    boolean existsByMemberAndIdLessThan(Member member, Long id);
}
