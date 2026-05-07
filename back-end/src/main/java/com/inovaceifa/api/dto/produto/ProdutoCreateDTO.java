package com.inovaceifa.api.dto.produto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoCreateDTO {

    @NotBlank
    private String nome;

    @NotBlank
    private String codigo;

    @NotBlank
    private String unidade;

    private String ativoNutr;

    @NotNull
    private Long grupoId;

    @NotNull
    private Long familiaId;

    private BigDecimal qtde;
    private BigDecimal vlrUnitario;

    private BigDecimal precoCusto;
}