package com.coredisc.infrastructure.repository.category;

import com.coredisc.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaCategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);
}
