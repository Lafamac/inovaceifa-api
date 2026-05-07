package com.inovaceifa.api.dto.relatorio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComparacaoTalhaoResponseDTO {

    private Long safraTalhaoId;
    private String talhaoNome;

    private BigDecimal custoReal;
    private BigDecimal receita;
    private BigDecimal lucro;
    private BigDecimal desvioPercentual;

    /* 🔥 NOVO — CATEGORIAS */

    private BigDecimal custoInsumos;
    private BigDecimal custoCombustivel;
    private BigDecimal custoMaoObra;
    private BigDecimal custoTerceiros;
    private BigDecimal custoMaquinas;
}