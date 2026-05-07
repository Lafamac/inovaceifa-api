package com.inovaceifa.api.dto.relatorio;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ComparacaoCompletaResponseDTO {

    private BigDecimal custoTotal;
    private BigDecimal receitaTotal;
    private BigDecimal lucroTotal;

    private BigDecimal areaTotal;
    private BigDecimal producaoTotal;

    private BigDecimal custoPorHectare;
    private BigDecimal custoPorSaca;

    private BigDecimal custoInsumos;
    private BigDecimal custoCombustivel;
    private BigDecimal custoMaoObra;
    private BigDecimal custoTerceiros;
    private BigDecimal custoMaquinas;
    private BigDecimal custoCompras;
    private BigDecimal custoAdministrativo;
}