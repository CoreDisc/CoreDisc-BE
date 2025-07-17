package com.coredisc.infrastructure.repository.category;

import com.coredisc.domain.category.Category;
import com.coredisc.domain.category.CategoryRepository;
import com.coredisc.infrastructure.repository.category.qeurydsl.QueryCategoryRepository;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryAdaptor implements CategoryRepository {

    private final JpaCategoryRepository jpaCategoryRepository;
    private final QueryCategoryRepository queryCategoryRepository;

    @Override
    public Optional<Category> findById(Long id){
        return jpaCategoryRepository.findById(id);
    }

    @Override
    public List<CategoryResponseDTO.CategoryDTO> findCategoryList(){
        return queryCategoryRepository.findCategoryList();
    }
}
