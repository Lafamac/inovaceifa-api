package com.inovaceifa.api.dto.segmentacao;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SegmentacaoFuncionarioResponseDTO {

    private Long id;

    private Long funcionarioId;
    private String funcionarioNome;

    private Long operacaoId;
    private String operacaoNome;

    private BigDecimal percentual;

    private Boolean ativo;
}
