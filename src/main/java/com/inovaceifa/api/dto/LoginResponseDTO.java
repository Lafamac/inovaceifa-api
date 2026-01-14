package com.inovaceifa.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private Integer perfilId;
    private String mensagem;
}
