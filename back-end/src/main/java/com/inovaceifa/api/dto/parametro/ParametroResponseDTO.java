package com.inovaceifa.api.dto.parametro;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ParametroResponseDTO {

    private BigDecimal percentual;
}