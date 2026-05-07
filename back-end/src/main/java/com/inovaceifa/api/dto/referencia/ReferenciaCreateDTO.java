package com.inovaceifa.api.dto.referencia;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReferenciaCreateDTO {

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;
    private String chave;
    private BigDecimal valor;
}