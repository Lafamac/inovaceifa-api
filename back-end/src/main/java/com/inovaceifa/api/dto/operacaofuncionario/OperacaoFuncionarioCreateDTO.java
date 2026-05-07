package com.inovaceifa.api.dto.operacaofuncionario;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperacaoFuncionarioCreateDTO {

    @NotNull(message = "Operação do talhão é obrigatória")
    private Long operacaoTalhaoId;

    @NotNull(message = "Funcionário é obrigatório")
    private Long funcionarioId;
    private BigDecimal valorUnitario;
    private BigDecimal horasTrabalhadas;
}