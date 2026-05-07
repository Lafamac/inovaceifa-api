package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "operacao_produto")
@Getter
@Setter
@NoArgsConstructor
public class OperacaoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* CONTEXTO */

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id")
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id")
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id")
    private Safra safra;

    /* RELACIONAMENTOS */

    @ManyToOne(optional = false)
    @JoinColumn(name = "operacao_talhao_id")
    private OperacaoTalhao operacaoTalhao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    /* DADOS */

    private BigDecimal quantidade;

    @Column(name = "vlr_unitario")
    private BigDecimal vlrUnitario;

    @Column(name = "vlr_total")
    private BigDecimal vlrTotal;
}