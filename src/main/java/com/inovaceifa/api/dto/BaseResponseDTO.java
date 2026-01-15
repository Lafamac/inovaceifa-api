package com.inovaceifa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponseDTO<T> {

    private boolean sucesso;
    private String mensagem;
    private T dados;
}
