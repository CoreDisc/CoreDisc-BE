package com.coredisc.infrastructure.repository.category.qeurydsl;

import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;

import java.util.List;

public interface QueryCategoryRepository {

    List<CategoryResponseDTO.CategoryDTO> findCategoryList();

    List<CategoryResponseDTO.CategoryDTO> findCategoryListByKeyword(Member member, String keyword);
}
