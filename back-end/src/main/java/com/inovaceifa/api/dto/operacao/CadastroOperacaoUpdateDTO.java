package com.inovaceifa.api.dto.operacao;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CadastroOperacaoUpdateDTO {

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

}