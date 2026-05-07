package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tipo_conta")
public class TipoConta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "arvore")
    private String arvore;

    @Column(name = "indice")
    private String indice;

    @Column(nullable = false)
    private Boolean ativo = true;
}