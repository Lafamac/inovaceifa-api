package com.inovaceifa.api.dto.talhao;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TalhaoUpdateDTO {

    @NotNull
    @Positive
    private BigDecimal area;

    private BigDecimal espacamentoRua;
    private BigDecimal espacamentoPlanta;
    private String material;

    private Long resistenciaFerrugemId;
    private Long sistemaCultivoId;
}