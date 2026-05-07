package com.inovaceifa.api.dto.safra;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SafraUpdateDTO {

    @NotBlank(message = "Nome da safra é obrigatório")
    @Size(max = 100, message = "Nome da safra deve ter no máximo 100 caracteres")
    private String nome;

    @NotNull(message = "Data inicial é obrigatória")
    private LocalDate dataInicial;

    @NotNull(message = "Data final é obrigatória")
    private LocalDate dataFinal;
}
