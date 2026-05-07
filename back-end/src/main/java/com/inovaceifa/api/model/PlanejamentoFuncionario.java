package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "planejamento_funcionario")
@Getter
@Setter
@NoArgsConstructor
public class PlanejamentoFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* RELACIONAMENTO */

    @ManyToOne(optional = false)
    @JoinColumn(name = "planejamento_operacao_id", nullable = false)
    private PlanejamentoOperacao planejamentoOperacao;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToOne
    @JoinColumn(name = "terceirizado_id")
    private Terceirizado terceirizado;

    @ManyToOne
    @JoinColumn(name = "turma_id")
    private TurmaTerceirizada turma;

    /* TIPO */

    @Column(name = "tipo_mao_obra", length = 20, nullable = false)
    private String tipoMaoObra;

    /* CAMPOS */

    @Column(name = "quantidade_pessoas")
    private Long quantidadePessoas = 1L; // ✅ ALTERADO PARA LONG

    @Column(name = "horas_previstas", precision = 10, scale = 2)
    private BigDecimal horasPrevistas;

    @Column(name = "custo_hora_previsto", precision = 15, scale = 2)
    private BigDecimal custoHoraPrevisto;

    @Column(name = "custo_total_previsto", precision = 15, scale = 2)
    private BigDecimal custoTotalPrevisto;

    private String observacao;

    private Boolean ativo = true;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
}