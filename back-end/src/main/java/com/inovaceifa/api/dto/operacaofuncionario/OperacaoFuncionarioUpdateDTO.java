package com.inovaceifa.api.dto.operacaofuncionario;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperacaoFuncionarioUpdateDTO {

    private BigDecimal horasTrabalhadas;
    private BigDecimal valorUnitario;
}