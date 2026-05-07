package com.inovaceifa.api.dto.relatorio;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ComparacaoResponseDTO {

    private Long produtoId;
    private String produtoNome;

    private BigDecimal quantidadePlanejada;
    private BigDecimal quantidadeRealizada;

    private BigDecimal diferencaQuantidade;

    private BigDecimal custoPlanejado;
    private BigDecimal custoRealizado;

    private BigDecimal diferencaCusto;
}