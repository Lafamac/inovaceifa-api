package com.inovaceifa.api.dto.relatorio;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class GestaoVistaDTO {

    private Long safraTalhaoId;
    private String talhaoNome;

    private BigDecimal area;
    private BigDecimal producao;

    /* 🔥 COMERCIAL */

    private BigDecimal quantidadeVendida;
    private BigDecimal estoque;
    private BigDecimal precoMedio;

    /* 🔥 PRODUTIVIDADE */

    private BigDecimal produtividade;

    /* 🔥 FINANCEIRO */

    private BigDecimal custoTotal;
    private BigDecimal custoPorHectare;
    private BigDecimal custoPorSaca;

    private BigDecimal receita;
    private BigDecimal lucro;

    /* 🔥 CATEGORIAS */

    private BigDecimal custoInsumos;
    private BigDecimal custoCombustivel;
    private BigDecimal custoMaoObra;
    private BigDecimal custoTerceiros;
    private BigDecimal custoMaquinas;
}