package com.inovaceifa.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ErrorResponseDTO {

    @Schema(example = "false")
    private boolean success;

    @Schema(example = "CPF já cadastrado")
    private String message;

    @Schema(example = "null")
    private Object data;
}
