package com.inovaceifa.api.service;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResultadoCalculoOS {

    private BigDecimal insumos;
    private BigDecimal maquinas;
    private BigDecimal maoObra;
    private BigDecimal combustivel;
    private BigDecimal total;

    private BigDecimal areaTotal;
    private BigDecimal horasMaquina;
    private BigDecimal litrosDiesel;
}