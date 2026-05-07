package com.inovaceifa.api.dto.talhao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Cadastro de talhão")
public class TalhaoCreateDTO {

    @NotBlank
    private String nome;

    @NotNull
    @Positive
    private BigDecimal area;

    private BigDecimal espacamentoRua;
    private BigDecimal espacamentoPlanta;
    private String material;

    private Long resistenciaFerrugemId;
    private Long sistemaCultivoId;
}