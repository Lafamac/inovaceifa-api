package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "operacao_funcionario")
@Getter
@Setter
@NoArgsConstructor
public class OperacaoFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* CONTEXTO */

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private Safra safra;

    /* RELACIONAMENTOS */

    @ManyToOne(optional = false)
    @JoinColumn(name = "operacao_talhao_id", nullable = false)
    private OperacaoTalhao operacaoTalhao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    /* DADOS */

    @Column(name = "horas_trabalhadas", precision = 10, scale = 2)
    private BigDecimal horasTrabalhadas;
}