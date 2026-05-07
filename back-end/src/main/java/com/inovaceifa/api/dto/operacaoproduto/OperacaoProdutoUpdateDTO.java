package com.inovaceifa.api.dto.operacaoproduto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OperacaoProdutoUpdateDTO {

    private BigDecimal quantidade;

    private BigDecimal vlrUnitario;
}