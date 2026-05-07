package com.inovaceifa.api.dto.horamaquina;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HoraMaquinaCreateDTO {

    @NotNull(message = "Máquina é obrigatória")
    private Long maquinaId;

    private Long funcionarioId;

    private Long operacaoTalhaoId;

    private String servicoExec;

    private String nroOs;

    private BigDecimal custoHora; // ✔ já estava certo

    @NotNull(message = "Data de execução é obrigatória")
    private LocalDate dataExecucao;

    @NotNull(message = "Horímetro inicial é obrigatório")
    @PositiveOrZero(message = "Horímetro inicial não pode ser negativo")
    private BigDecimal horimetroInicial;

    @NotNull(message = "Horímetro final é obrigatório")
    @Positive(message = "Horímetro final deve ser maior que zero")
    private BigDecimal horimetroFinal;
}