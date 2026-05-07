package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "operacao_talhao")
public class OperacaoTalhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id")
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id")
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id")
    private Safra safra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ordem_servico_id")
    private OrdemServico ordemServico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_talhao_id")
    private SafraTalhao safraTalhao;

    @ManyToOne
    @JoinColumn(name = "operacao_id")
    private CadastroOperacao operacao;

    /* 🔥 NOVO */

    @ManyToOne
    @JoinColumn(name = "operacao_talhao_tipo_id")
    private RefOperacaoTalhao operacaoTalhaoTipo;

    @Column(name = "area_trabalhada")
    private BigDecimal areaTrabalhada;

    @Column(name = "data_execucao")
    private LocalDate dataExecucao;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoInsumos;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoCombustivel;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoMaoObra;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoTerceiros;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoMaquina;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoTotal;
}