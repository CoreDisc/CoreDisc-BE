package com.coredisc.application.service.category;


import com.coredisc.presentation.dto.category.CategoryResponseDTO;

import java.util.List;

public interface CategoryQueryService {

    List<CategoryResponseDTO.CategoryDTO> getCategoryList();
}
