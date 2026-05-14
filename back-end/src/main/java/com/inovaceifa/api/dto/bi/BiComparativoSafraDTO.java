package com.inovaceifa.api.dto.bi;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BiComparativoSafraDTO {

    private Long safraId;

    private String safra;

    /* =========================
       FINANCEIRO
       ========================= */

    private BigDecimal receita;
    private BigDecimal custo;
    private BigDecimal lucro;

    private BigDecimal margem;

    /* =========================
       PRODUÇÃO
       ========================= */

    private BigDecimal area;

    private BigDecimal producao;

    private BigDecimal produtividade;

    /* =========================
       COMERCIAL
       ========================= */

    private BigDecimal vendido;

    private BigDecimal estoque;

    private BigDecimal precoMedio;

    private BigDecimal percentualComercializado;

    /* =========================
       INDICADORES
       ========================= */

    private BigDecimal custoPorHectare;

    private BigDecimal custoPorSaca;
}