package com.inovaceifa.api.dto.safra;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SafraResponseDTO {

    private Long id;
    private String nome;
    private LocalDate dataInicial;
    private LocalDate dataFinal;

    /* 🔵 NOVOS */
    private BigDecimal areaPlantada;
    private BigDecimal orcamentoPrevisto;
}