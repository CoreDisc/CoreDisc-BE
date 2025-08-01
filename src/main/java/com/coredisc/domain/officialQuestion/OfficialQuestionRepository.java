package com.coredisc.domain.officialQuestion;


import com.coredisc.domain.category.Category;
import com.coredisc.domain.member.Member;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OfficialQuestionRepository {

    OfficialQuestion save(OfficialQuestion officialQuestion);

    List<OfficialQuestion> findFavoritesByMember(Member member, Long cursorId, int pageSize);

    List<OfficialQuestion> findAllByMemberAndCursor(Member member, Long cursorId, int pageSize);

    List<OfficialQuestion> findAllByMemberAndCategory(Member member, Category category, Long cursorId, int pageSize);

    Long countOfficialQuestionByMember(Member member);

    Optional<OfficialQuestion> findById(Long id);

    List<Tuple> findTop5PopularQuestionsThisWeek(LocalDate startOfWeek, LocalDate endOfWeek);

}
