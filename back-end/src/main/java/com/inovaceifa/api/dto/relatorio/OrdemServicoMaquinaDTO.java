package com.inovaceifa.api.dto.relatorio;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrdemServicoMaquinaDTO {

    private String maquina;
    private String operador;
    private BigDecimal horas;
    private BigDecimal custoHora;
    private BigDecimal custoTotal;
}