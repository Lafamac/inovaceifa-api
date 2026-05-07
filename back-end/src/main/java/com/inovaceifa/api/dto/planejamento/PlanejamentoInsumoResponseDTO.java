package com.inovaceifa.api.dto.planejamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlanejamentoInsumoResponseDTO {

    private Long id;

    private Long planejamentoOperacaoId;
    private Long produtoId;

    private BigDecimal dosePorHa;
    private BigDecimal quantidadeTotal;

    private BigDecimal valorUnitarioPrevisto;
    private BigDecimal valorTotalPrevisto;

    private Boolean ativo;
}