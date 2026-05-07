package com.inovaceifa.api.dto.produto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovProdutoRequestDTO {

    private Long produtoId;
    private Long tipoMovimentoId;
    private LocalDate dataMovimento;
    private BigDecimal qtde;
    private BigDecimal vlrUnitario;
    private String numeroNotaFiscal;

    /* 🔵 NOVO */
    private String numeroOrdemServico;

    private LocalDate dataPagamento;
}