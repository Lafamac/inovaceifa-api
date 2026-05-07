package com.inovaceifa.api.dto.bi;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BiComparativoTalhaoDTO {

    private Long safraTalhaoId;

    private String talhao;

    private BigDecimal area;
    private BigDecimal producao;

    private BigDecimal produtividade;

    private BigDecimal custo;
    private BigDecimal custoPorHectare;
    private BigDecimal custoPorSaca;

    private BigDecimal receita;
    private BigDecimal lucro;

    private BigDecimal margem;

    private BigDecimal vendido;
    private BigDecimal estoque;

    private BigDecimal precoMedio;
}