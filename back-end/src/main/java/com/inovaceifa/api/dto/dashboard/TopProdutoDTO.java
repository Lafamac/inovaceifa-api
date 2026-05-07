package com.inovaceifa.api.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TopProdutoDTO {

    private String produto;
    private BigDecimal valor;
}