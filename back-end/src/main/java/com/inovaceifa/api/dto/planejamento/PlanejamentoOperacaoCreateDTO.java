package com.inovaceifa.api.dto.planejamento;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PlanejamentoOperacaoCreateDTO {

    @NotNull
    private Long safraTalhaoId;

    @NotNull
    private Long operacaoId;

    private LocalDate dataPrevista;

    @NotNull
    private BigDecimal areaPlanejada;

    private BigDecimal velocidade;
    private BigDecimal eficiencia;
    private BigDecimal horasPrevistas;
    private BigDecimal dieselPrevisto;

    private BigDecimal custoInsumos;
    private BigDecimal custoMaquinas;
    private BigDecimal custoCombustivel;
    private BigDecimal custoTotal;
}