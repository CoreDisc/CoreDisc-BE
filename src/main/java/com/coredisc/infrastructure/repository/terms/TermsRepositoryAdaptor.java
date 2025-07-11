package com.coredisc.infrastructure.repository.terms;

import com.coredisc.domain.terms.Terms;
import com.coredisc.domain.terms.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TermsRepositoryAdaptor implements TermsRepository {

    private final JpaTermsRepository jpaTermsRepository;

    @Override
    public List<Terms> findLatestTermsByType() {

        return jpaTermsRepository.findLatestTermsByType();
    }
}
