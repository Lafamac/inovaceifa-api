package com.inovaceifa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private Long id;
    private String nome;
    private String email;
    private String perfil;
}
