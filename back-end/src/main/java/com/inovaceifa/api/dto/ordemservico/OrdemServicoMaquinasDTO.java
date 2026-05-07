package com.inovaceifa.api.dto.ordemservico;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdemServicoMaquinasDTO {

    private List<MaquinaItem> maquinas;

    @Data
    public static class MaquinaItem {

        private Long maquinaId;

        private BigDecimal horimetroInicial;

        private BigDecimal horimetroFinal;
    }
}