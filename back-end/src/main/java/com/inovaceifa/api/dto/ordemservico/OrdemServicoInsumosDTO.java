package com.inovaceifa.api.dto.ordemservico;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdemServicoInsumosDTO {

    private List<InsumoItem> insumos;

    @Data
    public static class InsumoItem {

        private Long produtoId;

        private BigDecimal quantidade;

        private BigDecimal valorUnitario;
    }
}