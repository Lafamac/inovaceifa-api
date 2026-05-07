package com.inovaceifa.api.dto.operacaofuncionario;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OperacaoFuncionarioResponseDTO {

    private Long id;

    private Long operacaoTalhaoId;

    private Long funcionarioId;
    private String funcionarioNome;

    private BigDecimal horasTrabalhadas;
}