package com.inovaceifa.api.dto.administrativo;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdministrativoUpdateDTO {

    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String descricao;

    @Pattern(
            regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])$",
            message = "Mês/Ano deve estar no formato YYYY-MM"
    )
    private String mesAno;

    @Size(max = 20, message = "Unidade deve ter no máximo 20 caracteres")
    private String un;

    /* ================= REFERÊNCIAS ================= */

    private Long contaGerencialId;

    private Long despesaEducampoId;

    /* ================= PLANEJADO ================= */

    @PositiveOrZero(message = "Valor unitário planejado não pode ser negativo")
    private BigDecimal valorUnitPlanejado;

    @PositiveOrZero(message = "Quantidade planejada não pode ser negativa")
    private Long quantidadePlanejada;

    @PositiveOrZero(message = "Valor total planejado não pode ser negativo")
    private BigDecimal valorTotalPlanejado;

    @PositiveOrZero(message = "Valor por hectare planejado não pode ser negativo")
    private BigDecimal valorHaPlanejado;

    /* ================= REALIZADO ================= */

    @PositiveOrZero(message = "Valor unitário realizado não pode ser negativo")
    private BigDecimal valorUnitRealizado;

    @PositiveOrZero(message = "Quantidade realizada não pode ser negativa")
    private Long quantidadeRealizada;

    @PositiveOrZero(message = "Valor total realizado não pode ser negativo")
    private BigDecimal valorTotalRealizado;

    @PositiveOrZero(message = "Valor por hectare realizado não pode ser negativo")
    private BigDecimal valorHaRealizado;
}
