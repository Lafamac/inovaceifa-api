package com.inovaceifa.api.dto.administrativo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdministrativoResponseDTO {

    private Long id;

    private String descricao;
    private String mesAno;
    private String un;

    private Long fazendaId;
    private Long safraId;

    private Long contaGerencialId;
    private String contaGerencialDescricao;

    private Long despesaEducampoId;
    private String despesaEducampoDescricao;

    private BigDecimal valorUnitPlanejado;
    private Long quantidadePlanejada;
    private BigDecimal valorTotalPlanejado;
    private BigDecimal valorHaPlanejado;

    private BigDecimal valorUnitRealizado;
    private Long quantidadeRealizada;
    private BigDecimal valorTotalRealizado;
    private BigDecimal valorHaRealizado;
}