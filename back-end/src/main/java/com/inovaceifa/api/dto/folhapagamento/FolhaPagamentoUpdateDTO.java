package com.inovaceifa.api.dto.folhapagamento;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FolhaPagamentoUpdateDTO {

    private BigDecimal salarioBase;
    private BigDecimal encargos;
}