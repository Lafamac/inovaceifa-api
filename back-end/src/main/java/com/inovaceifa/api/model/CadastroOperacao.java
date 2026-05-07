package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cadastro_operacao")
public class CadastroOperacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cod_oper")
    private Long codOper;

    private String cultura;

    private String operacao;

    private String modalidade;

    private String deslocamento;

    private String atividade;

    @Column(name = "faixa_nominal")
    private BigDecimal faixaNominal;

    @Column(name = "velocidade_op")
    private BigDecimal velocidadeOp;

    @Column(name = "eficiencia_campo")
    private BigDecimal eficienciaCampo;

    @Column(name = "gasto_diesel")
    private BigDecimal gastoDiesel;

    @Column(name = "custo_hora_maquina", precision = 15, scale = 2)
    private BigDecimal custoHoraMaquina;

    private Boolean ativo = true;

}