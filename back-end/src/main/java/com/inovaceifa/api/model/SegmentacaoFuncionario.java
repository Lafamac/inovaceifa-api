package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "segmentacao_funcionario")
@Getter
@Setter
@NoArgsConstructor
public class SegmentacaoFuncionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Funcionario funcionario;

    @ManyToOne(optional = false)
    private CadastroOperacao operacao;

    private BigDecimal percentual;

    @ManyToOne
    private Proprietario proprietario;

    @ManyToOne
    private Fazenda fazenda;

    @ManyToOne
    private Safra safra;

    private Boolean ativo = true;
}