package com.inovaceifa.api.dto.segmentacao;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SegmentacaoFuncionarioCreateDTO {

    private Long funcionarioId;
    private Long operacaoId;
    private BigDecimal percentual;
}