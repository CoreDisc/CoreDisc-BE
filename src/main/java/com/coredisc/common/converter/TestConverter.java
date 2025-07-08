package com.coredisc.common.converter;

import com.coredisc.domain.test.Test;
import com.coredisc.presentation.dto.TestRequestDTO;
import com.coredisc.presentation.dto.TestResponseDTO;


public  class TestConverter {

    private TestConverter() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    public static Test toEntity(TestRequestDTO request) {
        return Test.of(request.getContent());
    }

    public static TestResponseDTO toResponse(Test entity) {
        return new TestResponseDTO(entity.getId(), entity.getContent());
    }
}