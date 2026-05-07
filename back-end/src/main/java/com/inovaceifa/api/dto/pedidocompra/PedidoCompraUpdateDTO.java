package com.inovaceifa.api.dto.pedidocompra;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PedidoCompraUpdateDTO {

    private LocalDate data;
    private Long statusId;
    private String fornecedorNome;
    private Long centroCustoId;
    private List<PedidoCompraItemDTO> itens;
}