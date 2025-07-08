package com.coredisc.presentation.controllerdocs;

import com.coredisc.common.apiPayload.ApiResponse;
import com.coredisc.presentation.dto.TestRequestDTO;
import com.coredisc.presentation.dto.TestResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Test", description = "Test 도메인 API")
public interface TestControllerDocs {

    @Operation(summary = "Test 생성")
    ApiResponse<TestResponseDTO> create(TestRequestDTO request);
}

