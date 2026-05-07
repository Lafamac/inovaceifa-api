package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contas_pagar")
@Getter
@Setter
@NoArgsConstructor
public class ContaPagar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String favorecido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private Safra safra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ref_despesa_id", nullable = false)
    private RefDespesa refDespesa;

    /* =========================================================
       NOVO - CENTRO DE CUSTO
       ========================================================= */

    @ManyToOne
    @JoinColumn(name = "centro_custo_id")
    private RefCentroCusto centroCusto;

    @Column(name = "n_nota_fiscal", length = 30)
    private String numeroNotaFiscal;

    @Column(name = "dt_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "dt_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "vlr_real", precision = 15, scale = 2)
    private BigDecimal vlrReal;

    @Column(name = "vlr_juros", precision = 15, scale = 2)
    private BigDecimal vlrJuros;

    @Column(name = "vlr_pago", precision = 15, scale = 2)
    private BigDecimal vlrPago;

    @Column(columnDefinition = "char(1)")
    private String baixada;
}