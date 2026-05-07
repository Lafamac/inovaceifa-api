package com.inovaceifa.api.dto.planejamento;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AdubacaoResponseDTO {

    private Long safraId;
    private List<AdubacaoTalhaoDTO> talhoes;
    private List<AdubacaoResumoDTO> totalGeral;
}