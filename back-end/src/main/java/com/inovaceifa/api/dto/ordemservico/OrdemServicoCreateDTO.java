package com.inovaceifa.api.dto.ordemservico;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrdemServicoCreateDTO {

    @NotNull(message = "Operação é obrigatória")
    private Long operacaoId;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private String status;

    private String observacao;

    private BigDecimal custoTotal;
}