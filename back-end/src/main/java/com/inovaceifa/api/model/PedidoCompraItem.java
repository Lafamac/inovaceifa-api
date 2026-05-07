package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_compra_item")
@Getter
@Setter
public class PedidoCompraItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_compra_id")
    private PedidoCompra pedidoCompra;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    private BigDecimal quantidade;

    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;
}