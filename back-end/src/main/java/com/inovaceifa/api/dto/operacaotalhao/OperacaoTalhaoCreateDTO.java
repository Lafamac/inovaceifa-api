package com.inovaceifa.api.dto.operacaotalhao;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OperacaoTalhaoCreateDTO {

    @NotNull
    private Long ordemServicoId;

    @NotNull
    private Long safraTalhaoId;

    /* 🔥 NOVO */

    private Long operacaoTalhaoTipoId;

    @NotNull
    private BigDecimal areaTrabalhada;

    private LocalDate dataExecucao;
}