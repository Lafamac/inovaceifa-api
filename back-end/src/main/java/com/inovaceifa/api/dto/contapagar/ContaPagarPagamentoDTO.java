package com.inovaceifa.api.dto.contapagar;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContaPagarPagamentoDTO {

    @NotNull(message = "Data de pagamento é obrigatória")
    private LocalDate dataPagamento;

    @PositiveOrZero(message = "Juros não pode ser negativo")
    private BigDecimal vlrJuros;
}
