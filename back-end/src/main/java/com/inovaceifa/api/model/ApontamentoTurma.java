package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "apontamento_turma")
@Getter
@Setter
@NoArgsConstructor
public class ApontamentoTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer diasTrabalhados;

    @Column(precision = 10, scale = 2)
    private BigDecimal quantidadeColhida;

    @Column(precision = 15, scale = 2)
    private BigDecimal valorTotal;

    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "turma_id")
    private com.inovaceifa.api.model.TurmaTerceirizada turma;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id")
    private com.inovaceifa.api.model.Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id")
    private com.inovaceifa.api.model.Safra safra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ordem_servico_id", nullable = false)
    private OrdemServico ordemServico;
}