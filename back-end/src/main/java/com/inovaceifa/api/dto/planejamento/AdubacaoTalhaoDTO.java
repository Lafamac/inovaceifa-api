package com.inovaceifa.api.dto.planejamento;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class AdubacaoTalhaoDTO {

    private Long talhaoId;
    private String talhaoNome;
    private BigDecimal area;
    private List<AdubacaoResumoDTO> produtos;
}