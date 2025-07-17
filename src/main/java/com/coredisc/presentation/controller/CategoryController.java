package com.coredisc.presentation.controller;

import com.coredisc.application.service.category.CategoryQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.controllerdocs.CategoryControllerDocs;
import com.coredisc.presentation.dto.category.CategoryResponseDTO;
import com.coredisc.presentation.dto.question.QuestionResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerDocs {

    private final CategoryQueryService categoryQueryService;

    @GetMapping("/categories")
    public ApiResponse<List<CategoryResponseDTO.CategoryDTO>> getCategoryList(){

        return ApiResponse.onSuccess(categoryQueryService.getCategoryList());
    }
}
