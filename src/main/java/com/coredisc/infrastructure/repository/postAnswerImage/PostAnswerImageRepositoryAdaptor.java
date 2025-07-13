package com.coredisc.infrastructure.repository.postAnswerImage;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.postAnswerImage.PostAnswerImage;
import com.coredisc.domain.postAnswerImage.PostAnswerImageRepository;
import com.coredisc.infrastructure.repository.postAnswerImage.queryDsl.QueryPostAnswerImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostAnswerImageRepositoryAdaptor implements PostAnswerImageRepository {

    private final JpaPostAnswerImageRepository jpaPostAnswerImageRepository;
    private final QueryPostAnswerImageRepository queryRepository;


    @Override
    public Boolean existsByLessId(Long id) {
        return jpaPostAnswerImageRepository.existsByIdLessThan(id);
    }

    @Override
    public List<PostAnswerImage> findImageAnswersByMember(Member member, Long cursorId, Pageable pageable) {
        return queryRepository.findImageAnswersByMember(member, cursorId, pageable);
    }
}
