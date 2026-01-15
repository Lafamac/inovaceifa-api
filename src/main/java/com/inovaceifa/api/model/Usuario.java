package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String senha;

    @Column(name = "perfil_id")
    private Integer perfilId;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Transient
    private String perfilDescricao;

}
