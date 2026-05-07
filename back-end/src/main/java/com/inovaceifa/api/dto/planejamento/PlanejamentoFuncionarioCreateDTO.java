package com.inovaceifa.api.dto.planejamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class PlanejamentoFuncionarioCreateDTO {

    @NotNull
    private String tipoMaoObra;

    private Long funcionarioId;
    private Long terceirizadoId;
    private Long turmaId;

    private Long quantidadePessoas;

    private BigDecimal horasPrevistas;
    private BigDecimal custoHoraPrevisto;

    private String observacao;
}