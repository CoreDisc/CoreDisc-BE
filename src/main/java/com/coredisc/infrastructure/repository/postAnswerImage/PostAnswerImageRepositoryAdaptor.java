package com.coredisc.infrastructure.repository.postAnswerImage;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.PostAnswerImage;
import com.coredisc.domain.postAnswer.PostAnswerRepository;
import com.coredisc.domain.postAnswerImage.PostAnswerImageRepository;
import com.coredisc.infrastructure.repository.postAnswerImage.queryDsl.QueryPostAnswerImageRepository;
import com.coredisc.infrastructure.repository.postAnswerImage.queryDsl.QueryPostAnswerImageRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostAnswerImageRepositoryAdaptor implements PostAnswerImageRepository {

    private final JpaPostAnswerImageRepository postAnswerImageRepository;
    // 조회용
    private final QueryPostAnswerImageRepositoryImpl queryPostAnswerImageRepository;


    @Override
    public void delete(PostAnswerImage postAnswerImage) {
        postAnswerImageRepository.delete(postAnswerImage);
    }

    @Override
    public List<PostAnswerImage> findImageAnswersByMember(Member member, Long cursorId, Pageable pageable) {
        return queryPostAnswerImageRepository.findImageAnswersByMember(member, cursorId, pageable);
    }

    @Override
    public boolean existsByMemberAndIdLessThan(Member member, Long id) {
        return queryPostAnswerImageRepository.existsByMemberAndIdLessThan(member, id);
    }
}
