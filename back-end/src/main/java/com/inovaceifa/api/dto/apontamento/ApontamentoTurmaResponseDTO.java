package com.inovaceifa.api.dto.apontamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ApontamentoTurmaResponseDTO {

    private Long id;

    private Long turmaId;
    private String turmaNome;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private Integer diasTrabalhados;
    private BigDecimal quantidadeColhida;
    private BigDecimal valorTotal;

    private String observacao;
}