package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "administrativo")
@Getter
@Setter
@NoArgsConstructor
public class Administrativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================
       RELACIONAMENTOS FIXOS
       ========================= */

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private com.inovaceifa.api.model.Safra safra;

    /* =========================
       REFERÊNCIAS
       ========================= */

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_gerencial_id")
    private ContaGerencial contaGerencial;

    @ManyToOne(optional = false)
    @JoinColumn(name = "despesa_educampo_id")
    private RefDespesa despesaEducampo;

    /* =========================
       IDENTIFICAÇÃO
       ========================= */

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(name = "mes_ano", length = 7)
    private String mesAno;

    @Column(length = 5)
    private String un;

    /* =========================
       PLANEJADO
       ========================= */

    @Column(name = "vlr_unit_planejado", precision = 15, scale = 2)
    private BigDecimal valorUnitPlanejado;

    @Column(name = "qtd_planejado")
    private Long quantidadePlanejada;

    @Column(name = "vlr_total_planejado", precision = 15, scale = 2)
    private BigDecimal valorTotalPlanejado;

    @Column(name = "vlr_ha_planejado", precision = 15, scale = 2)
    private BigDecimal valorHaPlanejado;

    @ManyToOne
    @JoinColumn(name = "tipo_rateio_id")
    private RefTipoRateio tipoRateio;

    @Column(name = "vlr_unit_realizado", precision = 15, scale = 2)
    private BigDecimal valorUnitRealizado;

    @Column(name = "qtd_realizada")
    private Long quantidadeRealizada;

    @Column(name = "vlr_total_realizado", precision = 15, scale = 2)
    private BigDecimal valorTotalRealizado;

    @Column(name = "vlr_ha_realizado", precision = 15, scale = 2)
    private BigDecimal valorHaRealizado;
}
