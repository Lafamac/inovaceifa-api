package com.inovaceifa.api.dto.apontamento;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ApontamentoTurmaCreateDTO {

    @NotNull
    private Long turmaId;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private Integer diasTrabalhados;

    private BigDecimal quantidadeColhida;

    private String observacao;

    // 🔥 CORREÇÃO OBRIGATÓRIA
    @NotNull(message = "Ordem de serviço é obrigatória")
    private Long ordemServicoId;
}