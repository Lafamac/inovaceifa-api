package com.inovaceifa.api.dto.contapagar;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContaPagarUpdateDTO {

    private String favorecido;

    private Long refDespesaId;

    private Long centroCustoId;

    private String numeroNotaFiscal;

    private LocalDate dataVencimento;

    private BigDecimal vlrReal;

}