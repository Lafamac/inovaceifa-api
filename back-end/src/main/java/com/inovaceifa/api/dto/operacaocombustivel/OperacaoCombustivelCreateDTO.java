package com.inovaceifa.api.dto.operacaocombustivel;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperacaoCombustivelCreateDTO {

    @NotNull(message = "Operação do talhão é obrigatória")
    private Long operacaoTalhaoId;

    @NotNull(message = "Máquina é obrigatória")
    private Long maquinaId;

    private BigDecimal litros;

    // 🔥 ADICIONADO
    private BigDecimal valorUnitario;
}