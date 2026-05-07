package com.inovaceifa.api.dto.turma;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TurmaUpdateDTO {

    private String nome;
    private String responsavel;
    private Long tipoPagamentoId;

    /* 🔥 NOVO */
    private Long operacaoId;
    private Long operacaoTalhaoId;
    private BigDecimal valorDiaria;
    private BigDecimal valorPorSaca;
    private Integer quantidadePessoas;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}