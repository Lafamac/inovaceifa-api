package com.inovaceifa.api.dto.financeiro;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardCategoriaDTO {

    private Long refDespesaId;
    private String descricao;
    private BigDecimal total;
    private BigDecimal percentual;
}