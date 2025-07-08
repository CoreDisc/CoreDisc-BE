package com.coredisc.presentation.controller;


import com.coredisc.application.service.test.TestService;
import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.common.converter.TestConverter;
import com.coredisc.presentation.controllerdocs.TestControllerDocs;
import com.coredisc.presentation.dto.TestRequestDTO;
import com.coredisc.presentation.dto.TestResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController implements TestControllerDocs {

    private final TestService testService;

    @PostMapping
    public  ApiResponse<TestResponseDTO>  create(@RequestBody TestRequestDTO request) {
        return
                ApiResponse.onSuccess(TestConverter.toResponse((
                testService.createTest(request)
        )));
    }
}