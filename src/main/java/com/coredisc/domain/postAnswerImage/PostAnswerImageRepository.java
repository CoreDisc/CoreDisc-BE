package com.coredisc.domain.postAnswerImage;

import com.coredisc.domain.member.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostAnswerImageRepository {

    Boolean existsByLessId(Long id);

    // QueryDsl
    List<PostAnswerImage> findImageAnswersByMember(Member member, Long cursorId, Pageable pageable);

}
