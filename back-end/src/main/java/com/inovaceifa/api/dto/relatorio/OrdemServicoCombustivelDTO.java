package com.inovaceifa.api.dto.relatorio;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrdemServicoCombustivelDTO {

    private String maquina;
    private BigDecimal litros;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
}