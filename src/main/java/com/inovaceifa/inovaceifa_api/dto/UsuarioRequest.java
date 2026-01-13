package com.inovaceifa.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {

    private String nome;
    private String email;
    private String senha;
    private Integer perfilId;
}
