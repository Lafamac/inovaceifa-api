package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "safras")
@Getter
@Setter
@NoArgsConstructor
public class Safra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @Column(nullable = false, length = 20)
    private String nome;

    @Column(name = "dt_inicial", nullable = false)
    private LocalDate dataInicial;

    @Column(name = "dt_final", nullable = false)
    private LocalDate dataFinal;

    /* =========================================================
       🔵 NOVOS CAMPOS FINANCEIROS
       ========================================================= */

    @Column(name = "area_plantada", precision = 15, scale = 2)
    private BigDecimal areaPlantada;

    @Column(name = "orcamento_previsto", precision = 15, scale = 2)
    private BigDecimal orcamentoPrevisto;
}