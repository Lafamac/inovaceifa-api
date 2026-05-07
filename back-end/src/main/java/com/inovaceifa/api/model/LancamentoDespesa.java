package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lancamento_despesa")
@Getter
@Setter
@NoArgsConstructor
public class LancamentoDespesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

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

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 30)
    private String origem; // MANUAL ou ORDEM_SERVICO

    @Column(length = 255)
    private String observacao;

    @Column(nullable = false, length = 20)
    private String statusPagamento = "PENDENTE";

    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();
}