package com.inovaceifa.api.dto.ordemservico;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrdemServicoUpdateDTO {

    private Long version;

    private Long planejamentoOperacaoId;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private String status;

    private String observacao;

    private BigDecimal custoTotal;
}