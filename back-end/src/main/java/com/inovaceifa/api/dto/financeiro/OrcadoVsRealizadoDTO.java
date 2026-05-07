package com.inovaceifa.api.dto.financeiro;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrcadoVsRealizadoDTO {

    private BigDecimal orcado;
    private BigDecimal realizado;
    private BigDecimal diferenca;

}