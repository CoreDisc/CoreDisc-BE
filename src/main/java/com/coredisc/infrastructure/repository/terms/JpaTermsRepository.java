package com.coredisc.infrastructure.repository.terms;

import com.coredisc.domain.terms.Terms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaTermsRepository extends JpaRepository<Terms, Long> {

    // 각 약관 종류에서 가장 최신 버전인 것들
    @Query("""
        SELECT t FROM Terms t
        WHERE t.version = (
            SELECT MAX(t2.version)
            FROM Terms t2
            WHERE t2.type = t.type
        )
    """)
    List<Terms> findLatestTermsByType();

    // 각 약관 종류에서 가장 최신이면서 필수인 것들
    @Query("""
    SELECT t FROM Terms t
    WHERE t.isRequired = true
      AND t.version = (
          SELECT MAX(t2.version)
          FROM Terms t2
          WHERE t2.type = t.type
            AND t2.isRequired = true
      )
""")
    List<Terms> findLatestRequiredTermsGroupedByType();

    Optional<Terms> findById(Long id);
}
