package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "proprietarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Proprietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relacionamento 1–1 com Usuario
     */
    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, columnDefinition = "char(11)", unique = true)
    private String cpf;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(length = 20)
    private String celular;

    @Column(length = 150)
    private String endereco;

    @Column(length = 100)
    private String bairro;

    @Column(length = 100)
    private String cidade;

    @Column(columnDefinition = "char(2)")
    private String estado;

    @Column(nullable = false)
    private Boolean ativo = true;
}
