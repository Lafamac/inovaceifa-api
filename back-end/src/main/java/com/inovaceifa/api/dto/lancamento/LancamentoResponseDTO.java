package com.inovaceifa.api.dto.lancamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class LancamentoResponseDTO {

    private Long id;

    private Long refDespesaId;
    private String descricaoDespesa;

    private Long centroCustoId;
    private String descricaoCentroCusto;

    private BigDecimal valor;
    private LocalDate data;
    private String origem;
    private String statusPagamento;
}