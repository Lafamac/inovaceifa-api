package com.inovaceifa.api.dto.planejamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlanejamentoComparativoDetalhadoDTO {

    private Long planejamentoOperacaoId;

    private BigDecimal previstoInsumos;
    private BigDecimal realInsumos;

    private BigDecimal previstoMaquina;
    private BigDecimal realMaquina;

    private BigDecimal previstoMaoObra;
    private BigDecimal realMaoObra;

    private BigDecimal previstoCombustivel;
    private BigDecimal realCombustivel;

    private BigDecimal previstoTotal;
    private BigDecimal realTotal;
}