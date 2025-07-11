package com.coredisc.domain.category;

import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(Long id);
}
