package com.inovaceifa.api.dto.safratalhao;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SafraTalhaoResponseDTO {

    private Long id;

    private RefDTO safra;
    private RefDTO talhao;
    private RefDTO cultura;

    private RefDTO resFerrugem;
    private RefDTO stCultivo;

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

    private Boolean ativo;

    /* =========================================================
       DTO AUXILIAR (PADRÃO FRONTEND)
       ========================================================= */

    @Data
    @Builder
    public static class RefDTO {
        private Long id;
        private String nome;
    }
}