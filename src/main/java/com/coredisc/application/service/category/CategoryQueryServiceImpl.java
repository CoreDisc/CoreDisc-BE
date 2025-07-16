package com.coredisc.application.service.category;

import com.coredisc.domain.category.CategoryRepository;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDTO.CategoryDTO> getCategoryList(){

        return categoryRepository.findCategoryList();
    }
}
