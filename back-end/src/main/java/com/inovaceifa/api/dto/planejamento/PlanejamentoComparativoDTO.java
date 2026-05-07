package com.inovaceifa.api.dto.planejamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlanejamentoComparativoDTO {

    private Long planejamentoOperacaoId;

    private BigDecimal custoPrevisto;
    private BigDecimal custoRealizado;

    private BigDecimal diferencaValor;
    private BigDecimal diferencaPercentual;
}