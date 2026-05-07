package com.inovaceifa.api.dto.pedidocompra;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PedidoCompraItemDTO {

    private Long produtoId;
    private BigDecimal quantidade;
    private BigDecimal valorUnitario;
}