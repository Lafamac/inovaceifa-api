package com.inovaceifa.api.dto.lancamento;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LancamentoCreateDTO {

    @NotNull
    private Long refDespesaId;

    @NotNull(message = "Centro de custo é obrigatório")
    private Long centroCustoId;

    @NotNull
    @Positive
    private BigDecimal valor;

    @NotNull
    private LocalDate data;

    private String observacao;
}