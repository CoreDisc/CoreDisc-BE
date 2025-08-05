package com.coredisc.application.service.category;

import com.coredisc.domain.category.CategoryRepository;
import com.coredisc.domain.member.Member;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponseDTO.CategoryDTO> getCategoryList(Member member){

        return categoryRepository.findCategoryList(member);
    }


    @Override
    public List<CategoryResponseDTO.CategoryDTO> getCategoryListByKeyword(Member member, String keyword) {

        return categoryRepository.findCategoryListByKeyword(member, keyword);
    }
}
