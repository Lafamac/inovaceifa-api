package com.inovaceifa.api.dto.ordemservico;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class OrdemServicoFromPlanejamentoDTO {

    @NotEmpty
    private List<Long> planejamentoIds;
}