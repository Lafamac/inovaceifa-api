package com.inovaceifa.api.dto.contapagar;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ContaPagarResponseDTO {

    private Long id;
    private String favorecido;
    private Long fazendaId;
    private Long safraId;

    private Long refDespesaId;
    private String descricaoDespesa;

    private Long centroCustoId;
    private String descricaoCentroCusto;

    private String numeroNotaFiscal;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;

    private BigDecimal vlrReal;
    private BigDecimal vlrJuros;
    private BigDecimal vlrPago;

    private String baixada;
}