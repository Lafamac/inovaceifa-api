package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "turmas_terceirizadas")
@Getter
@Setter
@NoArgsConstructor
public class TurmaTerceirizada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String responsavel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_pagamento_id", nullable = false)
    private RefTipoPagamento tipoPagamento;

    private BigDecimal valorDiaria;
    private BigDecimal valorPorSaca;
    private Integer quantidadePessoas;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private Boolean ativo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private Safra safra;

    @ManyToOne
    @JoinColumn(name = "operacao_id")
    private CadastroOperacao operacao;

    @ManyToOne
    @JoinColumn(name = "operacao_talhao_id")
    private OperacaoTalhao operacaoTalhao;
}