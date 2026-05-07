package com.inovaceifa.api.dto.operacaotalhao;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class OperacaoTalhaoResponseDTO {

    private Long id;

    private Long proprietarioId;
    private Long fazendaId;
    private Long safraId;

    private Long ordemServicoId;

    private Long safraTalhaoId;

    private Long talhaoId;
    private String talhaoNome;

    private BigDecimal areaTrabalhada;
    private LocalDate dataExecucao;

    /* 🔥 NOVO */

    private Long operacaoTalhaoTipoId;
    private String operacaoTalhaoTipoDescricao;

    private String ordemServicoStatus;

    private BigDecimal custoTotal;
    private BigDecimal custoInsumos;
    private BigDecimal custoCombustivel;
}