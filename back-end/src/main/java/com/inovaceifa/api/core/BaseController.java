package com.inovaceifa.api.core;

import com.inovaceifa.api.dto.ApiResponseDTO;
import org.springframework.http.ResponseEntity;

public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponseDTO<T>> ok(T data, String message) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(data, message)
        );
    }

}