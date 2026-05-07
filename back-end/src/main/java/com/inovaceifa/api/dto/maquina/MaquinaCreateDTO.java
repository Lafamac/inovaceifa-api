package com.inovaceifa.api.dto.maquina;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaquinaCreateDTO {

    @NotBlank(message = "Nome da máquina é obrigatório")
    @Size(max = 255)
    private String nome;

    @Size(max = 100)
    private String marca;

    @Size(max = 100)
    private String modelo;

    @Size(max = 255)
    private String descricao;

    @Min(value = 1900, message = "Ano de fabricação inválido")
    private Long anoFabricacao;

    @Size(max = 255)
    private String imagem;

    @PositiveOrZero(message = "Horímetro não pode ser negativo")
    private BigDecimal horimetro;

    /** Tabela de referência */
    @NotNull(message = "Tipo da máquina é obrigatório")
    private Long tipoMaquinaId;

    /* NOVOS CAMPOS */

    @NotNull(message = "Tipo de posse é obrigatório")
    private Long tipoPosseId;

    @PositiveOrZero
    private BigDecimal valorDiaria;

    private LocalDate inicioLocacao;
    private LocalDate fimLocacao;
    private Long diasContratados;

    @PositiveOrZero
    private BigDecimal valorTotalLocacao;
    private Boolean ativo;
}