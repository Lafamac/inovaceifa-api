package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "safra_talhoes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"safra_id", "talhao_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SafraTalhao {

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "talhao_id", nullable = false)
    private Talhao talhao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cultura_id", nullable = false)
    private RefCultura cultura;

    /* NOVOS RELACIONAMENTOS */

    @ManyToOne
    @JoinColumn(name = "id_res_ferrugem")
    private RefResFerrugem resFerrugem;

    @ManyToOne
    @JoinColumn(name = "id_st_cultivo")
    private RefStCultivo stCultivo;

    /* CAMPOS */

    @Column(name = "area_utilizada", nullable = false, precision = 10, scale = 2)
    private BigDecimal areaUtilizada;

    @Column(name = "esp_rua", precision = 10, scale = 2)
    private BigDecimal espRua;

    @Column(name = "esp_planta", precision = 10, scale = 2)
    private BigDecimal espPlanta;

    @Column(length = 100)
    private String material;

    @Column(name = "st_terra", length = 100)
    private String stTerra;

    private LocalDate vencContrato;

    private Boolean irrigacao;

    @Column(name = "est_litro_planta", precision = 10, scale = 2)
    private BigDecimal estLitroPlanta;

    @Column(name = "est_saca_hectare", precision = 10, scale = 2)
    private BigDecimal estimativaSacaHectare;

    @Column(name = "est_saca", precision = 10, scale = 2)
    private BigDecimal estimativaSaca;

    @Column(name = "producao_real", precision = 10, scale = 2)
    private BigDecimal producaoReal;

    @Column(name = "preco_saca", precision = 10, scale = 2)
    private BigDecimal precoSaca;

    private Boolean ativo = true;
}