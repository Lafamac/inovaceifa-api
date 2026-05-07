package com.inovaceifa.api.dto.planejamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Cadastro de máquina no planejamento")
public class PlanejamentoMaquinaCreateDTO {

    @Schema(example = "2")
    @NotNull
    private Long maquinaId;
}