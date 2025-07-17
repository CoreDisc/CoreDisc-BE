package com.coredisc.infrastructure.repository.category.qeurydsl;

import com.coredisc.presentation.dto.category.CategoryResponseDTO;

import java.util.List;

public interface QueryCategoryRepository {

    List<CategoryResponseDTO.CategoryDTO> findCategoryList();
}
