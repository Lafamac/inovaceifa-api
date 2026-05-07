package com.inovaceifa.api.dto.folhapagamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FolhaPagamentoResponseDTO {

    private Long id;

    private Long funcionarioId;
    private String funcionarioNome;

    private String mesAno;

    private BigDecimal salarioBase;
    private BigDecimal encargos;
    private BigDecimal total;

    private Boolean ativo;
}