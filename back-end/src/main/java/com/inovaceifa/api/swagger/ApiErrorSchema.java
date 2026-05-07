package com.inovaceifa.api.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Map;

@Data
@Schema(name = "ApiError")
public class ApiErrorSchema {

    @Schema(example = "Erro de validação")
    private String message;

    @Schema(example = "{\"nome\":\"Campo obrigatório\"}")
    private Map<String, String> errors;
}
