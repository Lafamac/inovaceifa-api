package com.inovaceifa.api.dto.ordemservico;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdemServicoTalhoesDTO {

    private List<TalhaoItem> talhoes;

    @Data
    public static class TalhaoItem {

        private Long talhaoId;

        private BigDecimal areaTrabalhada;
    }
}