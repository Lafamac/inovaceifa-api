package com.inovaceifa.api.dto.planejamento;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlanejamentoFuncionarioResponseDTO {

    private Long id;

    private String tipoMaoObra;

    private Long funcionarioId;
    private Long terceirizadoId;
    private Long turmaId;

    private Long quantidadePessoas;

    private BigDecimal horasPrevistas;
    private BigDecimal custoHoraPrevisto;
    private BigDecimal custoTotalPrevisto;

    private String observacao;
}