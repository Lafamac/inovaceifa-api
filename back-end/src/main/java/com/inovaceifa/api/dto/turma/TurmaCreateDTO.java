package com.inovaceifa.api.dto.turma;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TurmaCreateDTO {

    @NotBlank
    private String nome;

    private String responsavel;

    @NotNull
    private Long tipoPagamentoId;


    private Long operacaoId;
    private Long operacaoTalhaoId;

    private BigDecimal valorDiaria;
    private BigDecimal valorPorSaca;

    @NotNull
    private Integer quantidadePessoas;

    private LocalDate dataInicio;
    private LocalDate dataFim;

}