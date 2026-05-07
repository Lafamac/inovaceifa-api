package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "talhoes")
@Getter
@Setter
@NoArgsConstructor
public class Talhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @Column(name = "dt_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Column(name = "esp_rua", precision = 10, scale = 2)
    private BigDecimal espacamentoRua;

    @Column(name = "esp_planta", precision = 10, scale = 2)
    private BigDecimal espacamentoPlanta;

    @Column(length = 100)
    private String material;

    @ManyToOne
    @JoinColumn(name = "id_res_ferrugem")
    private RefResFerrugem resistenciaFerrugem;

    @ManyToOne
    @JoinColumn(name = "id_st_cultivo")
    private RefStCultivo sistemaCultivo;

    @Column(nullable = false)
    private Boolean ativo = true;
}