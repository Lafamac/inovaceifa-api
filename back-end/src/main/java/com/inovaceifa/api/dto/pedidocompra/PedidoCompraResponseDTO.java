package com.inovaceifa.api.dto.pedidocompra;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class PedidoCompraResponseDTO {

    private Long id;
    private LocalDate data;

    private Long statusId;

    // 🔥 ADICIONE ISSO
    private String statusDescricao;

    private BigDecimal valorTotal;
    private Boolean ativo;

    private List<PedidoCompraItemResponseDTO> itens;
}