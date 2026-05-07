package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "venda_producao")
@Getter
@Setter
@NoArgsConstructor
public class VendaProducao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* CONTEXTO */

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id")
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id")
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id")
    private Safra safra;

    /* RELACIONAMENTO */

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_talhao_id")
    private SafraTalhao safraTalhao;

    /* DADOS */

    @Column(precision = 15, scale = 2, nullable = false)
    private BigDecimal quantidade;

    @Column(name = "preco_unitario", precision = 15, scale = 2, nullable = false)
    private BigDecimal precoUnitario;

    @Column(name = "valor_total", precision = 15, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "data_venda", nullable = false)
    private LocalDate dataVenda;
}