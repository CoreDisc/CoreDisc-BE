package com.coredisc.domain.terms;

import java.util.List;

public interface TermsRepository {

    List<Terms> findLatestTermsByType();
}
