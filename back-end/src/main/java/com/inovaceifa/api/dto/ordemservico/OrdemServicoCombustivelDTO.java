package com.inovaceifa.api.dto.ordemservico;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrdemServicoCombustivelDTO {

    private Long maquinaId;

    private BigDecimal litros;
}