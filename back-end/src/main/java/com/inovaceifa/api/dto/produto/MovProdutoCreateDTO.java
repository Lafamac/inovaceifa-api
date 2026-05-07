package com.inovaceifa.api.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Movimentação de produto (entrada / saída)")
public class MovProdutoCreateDTO {

    @NotNull
    private Long produtoId;

    @NotNull
    private Long tipoMovimentoId;

    @NotNull
    private LocalDate dataMovimento;

    @NotNull
    @Positive
    private BigDecimal qtde;

    private BigDecimal vlrUnitario;

    private String numeroNotaFiscal;

    /* 🔵 NOVO */
    private String numeroOrdemServico;

    private LocalDate dataPagamento;
}