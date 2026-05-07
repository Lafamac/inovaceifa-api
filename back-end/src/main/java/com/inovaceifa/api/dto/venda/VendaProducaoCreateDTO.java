package com.inovaceifa.api.dto.venda;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class VendaProducaoCreateDTO {

    private Long safraTalhaoId;
    private BigDecimal quantidade;
    private BigDecimal precoUnitario;
    private LocalDate dataVenda;
}