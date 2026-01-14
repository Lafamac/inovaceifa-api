package com.inovaceifa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor

public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String email;
    private Integer perfilId;
}

