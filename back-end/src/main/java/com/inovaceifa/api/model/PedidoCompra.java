package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedido_compra")
@Getter
@Setter
public class PedidoCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* 🔥 ADICIONADO */
    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id")
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id")
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id")
    private Safra safra;

    private LocalDate data;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id")
    private RefPedidoCompraStatus status;

    private String fornecedorNome;

    @ManyToOne
    @JoinColumn(name = "centro_custo_id")
    private RefCentroCusto centroCusto;

    private BigDecimal valorTotal;

    @OneToMany(mappedBy = "pedidoCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoCompraItem> itens;

    @Column(nullable = false)
    private Boolean ativo = true;
}