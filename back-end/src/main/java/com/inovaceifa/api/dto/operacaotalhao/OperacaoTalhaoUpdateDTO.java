package com.inovaceifa.api.dto.operacaotalhao;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OperacaoTalhaoUpdateDTO {

    private BigDecimal areaTrabalhada;

    private LocalDate dataExecucao;
}