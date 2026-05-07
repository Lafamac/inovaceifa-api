package com.inovaceifa.api.dto.gastomaquina;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class GastoMaquinaResponseDTO {

    private Long id;

    private LocalDate data;
    private String descricao;
    private BigDecimal valor;

    private Long tipoGastoId;
    private String tipoGastoDescricao;

    private Long maquinaId;
    private String maquinaNome;

    private Long fazendaId;
    private Long safraId;

    private Long funcionarioId;
}
