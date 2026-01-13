package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tab_ref_usuario")
@Getter
@Setter
public class RefUsuario {

    @Id
    private Integer id;

    @Column(nullable = false, length = 50)
    private String descricao;
}
