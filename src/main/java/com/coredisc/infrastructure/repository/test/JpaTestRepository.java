package com.coredisc.infrastructure.repository.test;

import com.coredisc.domain.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTestRepository extends JpaRepository<Test,Long> {

}
