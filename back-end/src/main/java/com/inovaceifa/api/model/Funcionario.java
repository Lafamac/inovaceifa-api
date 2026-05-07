package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@NoArgsConstructor
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, columnDefinition = "char(11)", unique = true)
    private String cpf;

    @Column(length = 255)
    private String endereco;

    @Column(length = 200)
    private String bairro;

    @Column(length = 200)
    private String cidade;

    @Column(columnDefinition = "char(2)")
    private String estado;

    @Column(length = 200)
    private String email;

    @Column(length = 50)
    private String celular;

    @Column(length = 255)
    private String imagem;

    /* =========================
       NOVOS CAMPOS
       ========================= */

    @Column(length = 50)
    private String cargo;

    @Column(precision = 15, scale = 2)
    private BigDecimal salario;

    @Column(name = "dt_admissao")
    private LocalDate dtAdmissao;

    @Column(nullable = false)
    private Boolean ativo = true;
}