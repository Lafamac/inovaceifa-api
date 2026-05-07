package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ordem_servico")
@Getter
@Setter
@NoArgsConstructor
public class OrdemServico {

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

    /* NOVOS CAMPOS */

    @Column(name = "nr_os", length = 20)
    private String nrOs;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @ManyToOne
    @JoinColumn(name = "planejamento_operacao_id")
    private PlanejamentoOperacao planejamentoOperacao;

    /* OPERAÇÃO */

    @ManyToOne(optional = false)
    @JoinColumn(name = "operacao_id", nullable = false)
    private CadastroOperacao operacao;

    /* CAMPOS */

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(length = 20)
    private String status;

    @Column(length = 255)
    private String observacao;

    @Column(name = "custo_total", precision = 15, scale = 2)
    private BigDecimal custoTotal;
}