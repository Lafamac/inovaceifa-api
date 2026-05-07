package com.inovaceifa.api.dto.financeiro;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class DashboardFinanceiroDTO {

    private BigDecimal totalGeral;
    private List<DashboardCategoriaDTO> categorias;
}