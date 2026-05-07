package com.inovaceifa.api.dto.contapagar;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ContaPagarCreateDTO {

    @NotBlank(message = "Favorecido é obrigatório")
    private String favorecido;

    @NotNull(message = "Tipo de despesa é obrigatório")
    private Long refDespesaId;

    @NotNull(message = "Centro de custo é obrigatório")
    private Long centroCustoId;

    @Size(max = 50)
    private String numeroNotaFiscal;

    @NotNull
    private LocalDate dataVencimento;

    @NotNull
    @Positive
    private BigDecimal vlrReal;
}