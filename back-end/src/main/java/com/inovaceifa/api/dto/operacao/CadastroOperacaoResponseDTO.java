package com.inovaceifa.api.dto.operacao;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CadastroOperacaoResponseDTO {

    private Long id;

    private Long codOper;

    private String cultura;

    private String operacao;

    private String modalidade;

    private String deslocamento;

    private String atividade;

    private BigDecimal faixaNominal;

    private BigDecimal velocidadeOp;

    private BigDecimal eficienciaCampo;

    private BigDecimal gastoDiesel;

    private Boolean ativo;

}