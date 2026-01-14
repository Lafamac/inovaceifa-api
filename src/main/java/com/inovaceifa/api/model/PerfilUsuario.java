package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tab_ref_usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerfilUsuario {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "descricao", nullable = false)
    private String descricao;
}
