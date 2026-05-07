package com.inovaceifa.api.dto.operacaocombustivel;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperacaoCombustivelUpdateDTO {

    private BigDecimal litros;

    // 🔥 ADICIONADO
    private BigDecimal valorUnitario;
}