package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /* =========================================================
       DADOS BÁSICOS
       ========================================================= */

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 13)
    private String codigo;

    @Column(length = 30)
    private String unidade;

    @Column(name = "ativo_nutr", length = 50)
    private String ativoNutr;

    @Column(name = "preco_custo", precision = 15, scale = 4)
    private BigDecimal precoCusto;

    /* =========================================================
       ESTOQUE / VALORES
       ========================================================= */

    @Column(precision = 15, scale = 2)
    private BigDecimal qtde;

    @Column(name = "vlr_unitario", precision = 15, scale = 2)
    private BigDecimal vlrUnitario;

    @Column(name = "vlr_total", precision = 15, scale = 2)
    private BigDecimal vlrTotal;

    /* =========================================================
       NOVOS CAMPOS
       ========================================================= */

    @ManyToOne(optional = false)
    @JoinColumn(name = "grupo_id", nullable = false)
    private RefGrupo grupo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "familia_id", nullable = false)
    private RefFamilia familia;

    @Column(nullable = false)
    private Boolean ativo = true;

    /* =========================================================
       RELACIONAMENTOS
       ========================================================= */

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;
}