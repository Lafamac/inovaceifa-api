package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "planejamento_maquina")
@Getter
@Setter
@NoArgsConstructor
public class PlanejamentoMaquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "planejamento_operacao_id", nullable = false)
    private PlanejamentoOperacao planejamentoOperacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "maquina_id", nullable = false)
    private Maquina maquina;

    @Column(name = "horas_previstas", precision = 10, scale = 2)
    private BigDecimal horasPrevistas;

    @Column(name = "custo_hora", precision = 15, scale = 2)
    private BigDecimal custoHora;

    @Column(name = "custo_total", precision = 15, scale = 2)
    private BigDecimal custoTotal;

    @Column(nullable = false)
    private Boolean ativo = true;
}