package com.inovaceifa.api.dto.planejamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlanejamentoMaquinaResponseDTO {

    private Long id;

    private Long planejamentoOperacaoId;
    private Long maquinaId;

    private BigDecimal horasPrevistas;
    private BigDecimal custoHora;
    private BigDecimal custoTotal;

    private Boolean ativo;
}