package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "operacao_combustivel")
@Getter
@Setter
@NoArgsConstructor
public class OperacaoCombustivel {

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

    @Column(name = "valor_unitario", precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    /* RELACIONAMENTOS */

    @ManyToOne(optional = false)
    @JoinColumn(name = "operacao_talhao_id", nullable = false)
    private OperacaoTalhao operacaoTalhao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "maquina_id", nullable = false)
    private Maquina maquina;

    /* DADOS */

    @Column(precision = 10, scale = 2)
    private BigDecimal litros;
}