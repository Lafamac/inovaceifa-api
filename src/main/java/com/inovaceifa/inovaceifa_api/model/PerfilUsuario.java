package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tab_ref_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilUsuario {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "descricao", nullable = false, length = 50)
    private String descricao;
}
