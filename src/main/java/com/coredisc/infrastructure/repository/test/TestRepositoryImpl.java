package com.coredisc.infrastructure.repository.test;

import com.coredisc.domain.test.Test;
import com.coredisc.domain.test.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TestRepositoryImpl implements TestRepository {

    private final JpaTestRepository testRepository;

    @Override
    public Test save(Test test) {
        return testRepository.save(test);
    }

    @Override
    public Optional<Test> findById(Long id) {
        return testRepository.findById(id);
    }
}
