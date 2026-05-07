package com.inovaceifa.api.dto.horamaquina;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class HoraMaquinaResponseDTO {

    private Long id;

    private Long maquinaId;
    private String maquinaNome;

    private Long fazendaId;
    private Long safraId;

    private Long funcionarioId;

    private Long operacaoTalhaoId;

    private String servicoExec;
    private String nroOs;

    private LocalDate dataExecucao;

    private BigDecimal horimetroInicial;
    private BigDecimal horimetroFinal;
    private BigDecimal horasTrabalhadas;

    // 🔥 ADICIONADO
    private BigDecimal custoHora;
}