package com.coredisc.domain.test;


import java.util.Optional;

/**
 * 도메인 영역에서 TestRepository 는 명세 역할을 담당함.
 * 실제 repository 의 구현은 infras 패키지에 위치해야함.
 *
 */

public interface TestRepository {
    Test save(Test test);
    Optional<Test> findById(Long id);
}
