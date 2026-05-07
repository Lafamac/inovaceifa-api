package com.inovaceifa.api.dto.safratalhao;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SafraTalhaoCreateDTO {

    @NotNull
    private Long talhaoId;

    @NotNull
    private Long culturaId;

    private Long resFerrugemId;
    private Long stCultivoId;

    @NotNull
    @Positive
    private BigDecimal areaUtilizada;

    private BigDecimal espRua;
    private BigDecimal espPlanta;

    private String material;
    private String stTerra;

    private LocalDate vencContrato;

    private Boolean irrigacao;

    private BigDecimal estLitroPlanta;

    private BigDecimal estimativaSacaHectare;
    private BigDecimal estimativaSaca;
    private BigDecimal producaoReal;
    private BigDecimal precoSaca;
}