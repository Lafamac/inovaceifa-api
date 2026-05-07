package com.inovaceifa.api.dto.financeiro;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardMensalDTO {

    private Long ano;
    private Long mes;
    private BigDecimal total;
}