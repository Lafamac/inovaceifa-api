package com.inovaceifa.api.dto.turma;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class TurmaResponseDTO {

    private Long id;
    private String nome;
    private String responsavel;

    private Long tipoPagamentoId;
    private String descricaoTipoPagamento;

    /* 🔥 NOVO */
    private Long operacaoId;

    private BigDecimal valorDiaria;
    private BigDecimal valorPorSaca;
    private Integer quantidadePessoas;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean ativo;
}