package com.inovaceifa.api.dto.produto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private String codigo;
    private String unidade;
    private String ativoNutr;

    private BigDecimal qtde;
    private BigDecimal vlrUnitario;
    private BigDecimal vlrTotal;

    // 🔥 ADICIONADO
    private BigDecimal precoCusto;

    private Long grupoId;
    private String grupoDescricao;

    private Long familiaId;
    private String familiaDescricao;

    private Boolean ativo;

    private Long fazendaId;
}