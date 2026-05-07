package com.inovaceifa.api.dto.venda;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class VendaProducaoResponseDTO {

    private Long id;
    private String talhaoNome;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;
    private LocalDate dataVenda;
}