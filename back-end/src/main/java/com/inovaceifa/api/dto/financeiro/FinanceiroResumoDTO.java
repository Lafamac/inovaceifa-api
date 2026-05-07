package com.inovaceifa.api.dto.financeiro;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FinanceiroResumoDTO {

    private BigDecimal totalDespesas;
    private BigDecimal totalPago;
    private BigDecimal totalPendente;
    private Long quantidadeLancamentos;
}