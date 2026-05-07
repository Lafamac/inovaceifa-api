package com.inovaceifa.api.dto.ordemservico;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdemServicoFuncionariosDTO {

    private List<FuncionarioItem> funcionarios;

    @Data
    public static class FuncionarioItem {

        private Long funcionarioId;

        private BigDecimal horasTrabalhadas;
    }
}