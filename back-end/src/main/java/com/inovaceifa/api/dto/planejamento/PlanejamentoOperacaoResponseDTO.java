package com.inovaceifa.api.dto.planejamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class PlanejamentoOperacaoResponseDTO {

    private Long id;

    private Long safraTalhaoId;
    private Long operacaoId;

    private LocalDate dataPrevista;
    private BigDecimal areaPlanejada;

    private BigDecimal velocidade;
    private BigDecimal eficiencia;
    private BigDecimal horasPrevistas;
    private BigDecimal dieselPrevisto;

    private BigDecimal custoInsumos;
    private BigDecimal custoMaquinas;
    private BigDecimal custoCombustivel;
    private BigDecimal custoTotal;

    private String status;
}