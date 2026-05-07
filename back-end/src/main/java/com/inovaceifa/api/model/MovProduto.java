package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mov_produtos")
@Getter
@Setter
@NoArgsConstructor
public class MovProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================================================
       RELACIONAMENTOS
       ========================================================= */

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private Safra safra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_mov_id", nullable = false)
    private RefTipoMovProduto tipoMovimento;

    /* =========================================================
       DADOS DA MOVIMENTAÇÃO
       ========================================================= */

    @Column(name = "dt_mov", nullable = false)
    private LocalDate dataMovimento;

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal qtde;

    @Column(name = "vlr_unitario", precision = 15, scale = 2)
    private BigDecimal vlrUnitario;

    @Column(name = "vlr_total", precision = 15, scale = 2)
    private BigDecimal vlrTotal;

    @Column(name = "nr_nota_fiscal", length = 20)
    private String numeroNotaFiscal;

    /* 🔵 NOVO CAMPO */
    @Column(name = "nr_os", length = 20)
    private String numeroOrdemServico;

    @Column(name = "dt_pagamento")
    private LocalDate dataPagamento;
}