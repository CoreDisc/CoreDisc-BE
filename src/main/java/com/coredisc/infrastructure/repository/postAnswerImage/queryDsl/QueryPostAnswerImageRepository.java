package com.coredisc.infrastructure.repository.postAnswerImage.queryDsl;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.postAnswerImage.PostAnswerImage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryPostAnswerImageRepository {

    List<PostAnswerImage> findImageAnswersByMember(Member member, Long cursorId, Pageable pageable);
    boolean existsByMemberAndIdLessThan(Member member, Long id);
}
