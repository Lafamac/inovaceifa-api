package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "horas_maquinas")
@Getter
@Setter
@NoArgsConstructor
public class HoraMaquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================================================
       RELACIONAMENTOS
       ========================================================= */

    @ManyToOne(optional = false)
    @JoinColumn(name = "maquina_id", nullable = false)
    private Maquina maquina;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private Safra safra;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @Column(name = "custo_hora", precision = 10, scale = 2)
    private BigDecimal custoHora;

    @ManyToOne
    @JoinColumn(name = "operacao_talhao_id")
    private OperacaoTalhao operacaoTalhao;

    /* =========================================================
       CAMPOS EXISTENTES
       ========================================================= */

    @Column(name = "servico_exec", length = 200)
    private String servicoExec;

    @Column(name = "nro_os", length = 50)
    private String nroOs;

    @Column(name = "data_execucao", nullable = false)
    private LocalDate dataExecucao;

    @Column(name = "horimetro_inicial", precision = 10, scale = 2, nullable = false)
    private BigDecimal horimetroInicial;

    @Column(name = "horimetro_final", precision = 10, scale = 2, nullable = false)
    private BigDecimal horimetroFinal;

    @Column(name = "horas_trabalhadas", precision = 10, scale = 2, nullable = false)
    private BigDecimal horasTrabalhadas;
}