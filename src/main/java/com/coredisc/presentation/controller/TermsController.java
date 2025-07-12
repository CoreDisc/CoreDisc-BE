package com.coredisc.presentation.controller;

import com.coredisc.application.service.terms.TermsQueryService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.TermsConverter;
import com.coredisc.domain.terms.Terms;
import com.coredisc.presentation.controllerdocs.TermsControllerDocs;
import com.coredisc.presentation.dto.terms.TermsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermsController implements TermsControllerDocs {

    private final TermsQueryService termsQueryService;

    @Override
    @GetMapping
    public ApiResponse<List<TermsResponseDTO.TermsDTO>> getTermsList() {

        List<Terms> termsList = termsQueryService.getTermsList();
        List<TermsResponseDTO.TermsDTO> termsDTOS = termsList.stream()
                .map(TermsConverter::toTermsListDTO)
                .toList();

        return ApiResponse.onSuccess(termsDTOS);
    }
}
