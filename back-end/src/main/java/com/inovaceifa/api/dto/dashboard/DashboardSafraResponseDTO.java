package com.inovaceifa.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardSafraResponseDTO {

    /* =========================
       FINANCEIRO
       ========================= */

    private BigDecimal custoTotal;
    private BigDecimal custoPorHectare;
    private BigDecimal custoPorSaca;

    private BigDecimal receitaTotal;
    private BigDecimal lucroTotal;

    private BigDecimal margemLucro;

    /* =========================
       PRODUÇÃO
       ========================= */

    private BigDecimal areaTotal;
    private BigDecimal producaoTotal;

    /* =========================
       COMERCIAL
       ========================= */

    private BigDecimal totalVendido;
    private BigDecimal estoqueTotal;
    private BigDecimal precoMedio;

    private BigDecimal percentualComercializado;

    /* =========================
       CUSTOS
       ========================= */

    private BigDecimal custoInsumos;
    private BigDecimal custoMaquinas;
    private BigDecimal custoCompras;

    /* =========================
       GRÁFICOS
       ========================= */

    private List<TopProdutoDTO> topProdutos;
    private List<DashboardTalhaoDTO> porTalhao;
}