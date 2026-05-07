package com.inovaceifa.api.dto.tipoconta;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TipoContaResponseDTO {

    private Long id;
    private String arvore;
    private String indice;
    private Boolean ativo;
}