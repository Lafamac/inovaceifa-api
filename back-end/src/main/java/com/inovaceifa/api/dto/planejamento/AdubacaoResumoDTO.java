package com.inovaceifa.api.dto.planejamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdubacaoResumoDTO {

    private Long produtoId;
    private String produtoNome;
    private BigDecimal quantidadeTotal;
}