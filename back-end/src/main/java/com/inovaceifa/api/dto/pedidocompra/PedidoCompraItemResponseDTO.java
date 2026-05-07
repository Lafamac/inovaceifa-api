package com.inovaceifa.api.dto.pedidocompra;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PedidoCompraItemResponseDTO {

    private Long produtoId;
    private String produtoNome;

    private BigDecimal quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;
}