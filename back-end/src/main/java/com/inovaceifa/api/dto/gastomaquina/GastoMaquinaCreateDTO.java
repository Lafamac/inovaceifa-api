package com.inovaceifa.api.dto.gastomaquina;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GastoMaquinaCreateDTO {

    @NotNull(message = "Tipo de gasto é obrigatório")
    private Long tipoGastoId;

    @NotNull(message = "Máquina é obrigatória")
    private Long maquinaId;

    private Long funcionarioId;

    @NotNull(message = "Data do gasto é obrigatória")
    private LocalDate data;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor do gasto deve ser maior que zero")
    private BigDecimal valor;
}
