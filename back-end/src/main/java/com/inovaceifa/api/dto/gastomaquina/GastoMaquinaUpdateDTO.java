package com.inovaceifa.api.dto.gastomaquina;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GastoMaquinaUpdateDTO {

    @NotNull(message = "Data do gasto é obrigatória")
    private LocalDate data;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 255)
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @Positive(message = "Valor do gasto deve ser maior que zero")
    private BigDecimal valor;

    private Long funcionarioId;
}
