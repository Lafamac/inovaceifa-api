package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "folha_pagamento")
public class FolhaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* RELACIONAMENTOS */

    @ManyToOne(optional = false)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id")
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id")
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id")
    private Safra safra;

    /* CAMPOS */

    @Column(name = "mes_ano", length = 7)
    private String mesAno; // ex: 2025-01

    @Column(name = "salario_base", precision = 15, scale = 2)
    private BigDecimal salarioBase;

    @Column(name = "encargos", precision = 15, scale = 2)
    private BigDecimal encargos;

    @Column(name = "total", precision = 15, scale = 2)
    private BigDecimal total;

    private Boolean ativo = true;

    /* 🔥 MÉTODO CALCULADO (PADRÃO EXCEL) */

    @Transient
    public BigDecimal getSalarioTotal() {

        BigDecimal base = salarioBase != null ? salarioBase : BigDecimal.ZERO;
        BigDecimal enc = encargos != null ? encargos : BigDecimal.ZERO;

        return base.add(enc);
    }
}