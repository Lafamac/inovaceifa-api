package com.inovaceifa.api.dto.administrativo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdministrativoRequestDTO {

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    private String mesAno;

    private String un;

    // 🔥 REFERÊNCIAS (OBRIGATÓRIAS)
    @NotNull(message = "Conta gerencial é obrigatória")
    private Long contaGerencialId;


    @NotNull(message = "Despesa Educampo é obrigatória")
    private Long despesaEducampoId;

    /* ================= PLANEJADO ================= */

    private BigDecimal valorUnitPlanejado;
    private Long quantidadePlanejada;
    private BigDecimal valorTotalPlanejado;
    private BigDecimal valorHaPlanejado;

    /* ================= REALIZADO ================= */

    private BigDecimal valorUnitRealizado;
    private Long quantidadeRealizada;
    private BigDecimal valorTotalRealizado;
    private BigDecimal valorHaRealizado;
}
