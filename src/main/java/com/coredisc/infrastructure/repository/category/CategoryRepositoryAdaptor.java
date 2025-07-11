package com.coredisc.infrastructure.repository.category;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdaptor implements CategoryRepository {

    private final JpaCategoryRepository jpaMemberRepository;

    @Override
    public Optional<Category> findById(Long id){
        return jpaMemberRepository.findById(id);
    }
}
