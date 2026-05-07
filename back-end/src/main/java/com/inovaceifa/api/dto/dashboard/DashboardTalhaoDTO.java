package com.inovaceifa.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardTalhaoDTO {

    private String talhao;

    private BigDecimal custo;
    private BigDecimal receita;
    private BigDecimal lucro;

    /* 🔥 NOVO */

    private BigDecimal vendido;
    private BigDecimal estoque;
    private BigDecimal precoMedio;
    private BigDecimal produtividade;
}