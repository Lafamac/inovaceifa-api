package com.inovaceifa.api.dto.produto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoUpdateDTO {

    private String nome;
    private String codigo;
    private String unidade;
    private String ativoNutr;

    private Long grupoId;
    private Long familiaId;

    private BigDecimal qtde;
    private BigDecimal vlrUnitario;
    private BigDecimal precoCusto;
}