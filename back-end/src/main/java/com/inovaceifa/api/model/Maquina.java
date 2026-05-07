package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "maquinas")
@Getter
@Setter
@NoArgsConstructor
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(length = 100)
    private String marca;

    @Column(length = 100)
    private String modelo;

    @Column(length = 255)
    private String descricao;

    @Column(name = "ano_fabricacao")
    private Long anoFabricacao;

    @Column(length = 255)
    private String imagem;

    @Column(precision = 10, scale = 2)
    private BigDecimal horimetro;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_maquina_id", nullable = false)
    private RefTipoMaquina tipoMaquina;

    /* =========================================================
       🆕 NOVOS CAMPOS — POSSE DA MÁQUINA
       ========================================================= */

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_posse_id", nullable = false)
    private RefTipoPosseMaquina tipoPosse;

    @Column(name = "valor_diaria", precision = 15, scale = 2)
    private BigDecimal valorDiaria;

    @Column(name = "inicio_locacao")
    private LocalDate inicioLocacao;

    @Column(name = "fim_locacao")
    private LocalDate fimLocacao;

    @Column(name = "dias_contratados")
    private Long diasContratados;

    @Column(name = "valor_total_locacao", precision = 15, scale = 2)
    private BigDecimal valorTotalLocacao;
}