package com.coredisc.application.service.category;


import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;

import java.util.List;

public interface CategoryQueryService {

    List<CategoryResponseDTO.CategoryDTO> getCategoryList(Member member);

    List<CategoryResponseDTO.CategoryDTO> getCategoryListByKeyword(Member member, String keyword);
}
