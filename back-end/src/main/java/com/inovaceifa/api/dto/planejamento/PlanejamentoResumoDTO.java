package com.inovaceifa.api.dto.planejamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PlanejamentoResumoDTO {

    private Long planejamentoOperacaoId;

    private BigDecimal areaPlanejada;

    // custos
    private BigDecimal custoInsumos;
    private BigDecimal custoMaquina;
    private BigDecimal custoMaoObra;
    private BigDecimal custoCombustivel;

    private BigDecimal custoTotal;

    // indicadores
    private BigDecimal custoPorHectare;
    private BigDecimal custoPorSaca;

    // 🔥 ADICIONADO (SEM ALTERAR O RESTO)
    private List<InsumoDTO> insumos;

    // 🔥 ADICIONADO (CLASSE INTERNA)
    @Data
    @Builder
    public static class InsumoDTO {
        private String produtoNome;
        private BigDecimal quantidadeTotal;
    }
}