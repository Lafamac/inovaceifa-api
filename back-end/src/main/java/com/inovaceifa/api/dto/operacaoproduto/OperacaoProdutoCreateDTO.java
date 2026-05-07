package com.inovaceifa.api.dto.operacaoproduto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
public class OperacaoProdutoCreateDTO {

    @NotNull
    private Long operacaoTalhaoId;

    @NotNull
    private Long produtoId;

    private BigDecimal quantidade;

    private BigDecimal vlrUnitario;
}