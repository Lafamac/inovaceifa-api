package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "planejamento_insumo")
@Getter
@Setter
@NoArgsConstructor
public class PlanejamentoInsumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "planejamento_operacao_id", nullable = false)
    private PlanejamentoOperacao planejamentoOperacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "dose_por_ha", nullable = false, precision = 10, scale = 4)
    private BigDecimal dosePorHa;

    @Column(name = "quantidade_total", precision = 15, scale = 4)
    private BigDecimal quantidadeTotal;

    @Column(name = "valor_unitario_previsto", precision = 15, scale = 4)
    private BigDecimal valorUnitarioPrevisto;

    @Column(name = "valor_total_previsto", precision = 15, scale = 4)
    private BigDecimal valorTotalPrevisto;

    @Column(nullable = false)
    private Boolean ativo = true;
}
