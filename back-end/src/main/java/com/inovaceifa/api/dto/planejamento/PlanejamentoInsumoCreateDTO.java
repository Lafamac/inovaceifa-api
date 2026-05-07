package com.inovaceifa.api.dto.planejamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Cadastro de insumo no planejamento")
public class PlanejamentoInsumoCreateDTO {

    @Schema(example = "5")
    @NotNull
    private Long produtoId;

    @Schema(example = "2.5")
    @NotNull
    @Positive
    private BigDecimal dosePorHa;

    @Schema(example = "45.50")
    private BigDecimal valorUnitarioPrevisto;
}