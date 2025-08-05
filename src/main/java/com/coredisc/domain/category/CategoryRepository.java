package com.coredisc.domain.category;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(Long id);

    List<CategoryResponseDTO.CategoryDTO> findCategoryList();

    List<CategoryResponseDTO.CategoryDTO> findCategoryListByKeyword(Member member, String keyword);
}
