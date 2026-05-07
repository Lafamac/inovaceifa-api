package com.inovaceifa.api.dto.folhapagamento;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FolhaPagamentoCreateDTO {

    @NotNull
    private Long funcionarioId;

    @NotNull
    private String mesAno;

    @NotNull
    private BigDecimal salarioBase;

    private BigDecimal encargos;
}