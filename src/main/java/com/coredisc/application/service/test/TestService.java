package com.coredisc.application.service.test;


import com.coredisc.common.converter.TestConverter;
import com.coredisc.domain.test.Test;
import com.coredisc.domain.test.TestRepository;
import com.coredisc.presentation.dto.TestRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    @Transactional
    public Test createTest(TestRequestDTO request) {
        Test test = TestConverter.toEntity(request);
        return testRepository.save(test);
    }
}