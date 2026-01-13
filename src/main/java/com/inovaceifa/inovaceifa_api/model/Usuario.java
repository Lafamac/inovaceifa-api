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

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    /**
     * Relacionamento com tabela de referência tab_ref_usuario
     * Muitos usuários podem ter o mesmo perfil
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilUsuario perfil;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    /**
     * Define automaticamente a data de criação
     */
    @PrePersist
    protected void onCreate() {
        this.criadoEm = LocalDateTime.now();
    }
}
