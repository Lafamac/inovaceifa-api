package com.inovaceifa.api.dto.produto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class MovProdutoResponseDTO {

    private Long id;
    private Long produtoId;
    private String produtoNome;
    private Long fazendaId;
    private Long safraId;
    private Long tipoMovimentoId;
    private String tipoMovimentoDescricao;
    private LocalDate dataMovimento;
    private BigDecimal qtde;
    private BigDecimal vlrUnitario;
    private BigDecimal vlrTotal;
    private String numeroNotaFiscal;

    /* 🔵 NOVO */
    private String numeroOrdemServico;

    private LocalDate dataPagamento;
}