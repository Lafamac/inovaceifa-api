package com.inovaceifa.api.dto.relatorio;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class GestaoVistaResponseDTO {

    private List<GestaoVistaDTO> itens;

    private BigDecimal totalArea;
    private BigDecimal totalProducao;
    private BigDecimal totalVendido;
    private BigDecimal totalEstoque;

    private BigDecimal totalCusto;
    private BigDecimal totalReceita;
    private BigDecimal totalLucro;

    private BigDecimal custoPorHectare;
    private BigDecimal custoPorSaca;
    private BigDecimal produtividadeMedia;

    private GestaoVistaDTO melhorTalhao;
    private GestaoVistaDTO piorTalhao;
}