package com.inovaceifa.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ApiResponse")
public class ApiResponseDTO<T> {

    @Schema(
            description = "Indica se a requisição foi bem sucedida",
            example = "true"
    )
    private boolean success;

    @Schema(
            description = "Mensagem de retorno para o cliente",
            example = "Operação realizada com sucesso"
    )
    private String message;

    @Schema(
            description = "Dados retornados pela API"
    )
    private T data;

    /* =========================================================
       SUCESSO
       ========================================================= */
    public static <T> ApiResponseDTO<T> success(T data, String message) {
        return ApiResponseDTO.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    /* =========================================================
       ERRO COM DADOS (ex: erros de validação)
       ========================================================= */
    public static <T> ApiResponseDTO<T> error(String message, T data) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponseDTO<T> validationError(String message, T errors) {
        return new ApiResponseDTO<>(false, message, errors);
    }

    /* =========================================================
       ERRO SIMPLES
       ========================================================= */
    public static <T> ApiResponseDTO<T> error(String message) {
        return ApiResponseDTO.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }


}

