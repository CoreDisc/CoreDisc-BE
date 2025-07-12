package com.coredisc.domain.terms;

import java.util.List;
import java.util.Optional;

public interface TermsRepository {

    List<Terms> findLatestTermsByType();

    List<Terms> findLatestRequiredTermsGroupedByType();

    Optional<Terms> findById(Long id);
}
