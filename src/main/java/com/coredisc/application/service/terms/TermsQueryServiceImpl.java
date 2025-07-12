package com.coredisc.application.service.terms;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.TermsHandler;
import com.coredisc.domain.terms.Terms;
import com.coredisc.domain.terms.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TermsQueryServiceImpl implements TermsQueryService {

    private final TermsRepository termsRepository;

    @Override
    public List<Terms> getTermsList() {

        List<Terms> termsList = termsRepository.findLatestTermsByType();
        if (termsList.isEmpty()) {
            throw new TermsHandler(ErrorStatus.TERMS_NOT_FOUND);
        }

        return termsRepository.findLatestTermsByType();
    }
}
