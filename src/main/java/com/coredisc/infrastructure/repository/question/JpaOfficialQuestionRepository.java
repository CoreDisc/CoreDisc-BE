package com.coredisc.infrastructure.repository.question;

import com.coredisc.domain.member.Member;
import com.coredisc.domain.officialQuestion.OfficialQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaOfficialQuestionRepository extends JpaRepository<OfficialQuestion, Long> {

     Long countOfficialQuestionByMember(Member member);

     @Query("SELECT o.isFavorite FROM OfficialQuestion o WHERE o.id = :id")
     Boolean findIsFavoriteById(@Param("id") Long id);
}
