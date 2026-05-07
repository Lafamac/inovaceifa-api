package com.inovaceifa.api.dto.produto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MovProdutoUpdateDTO {

    @NotNull(message = "Data da movimentação é obrigatória")
    private LocalDate dataMovimento;

    @Positive(message = "Quantidade deve ser maior que zero")
    private BigDecimal qtde;

    @PositiveOrZero(message = "Valor unitário não pode ser negativo")
    private BigDecimal vlrUnitario;

    @Size(max = 50)
    private String numeroNotaFiscal;

    /* 🔵 NOVO */
    @Size(max = 20)
    private String numeroOrdemServico;

    private LocalDate dataPagamento;
}