package com.inovaceifa.api.dto.operacaoproduto;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class OperacaoProdutoResponseDTO {

    private Long id;

    private Long operacaoTalhaoId;

    private Long produtoId;
    private String produtoNome;

    private BigDecimal quantidade;
    private BigDecimal vlrUnitario;
    private BigDecimal vlrTotal;
}