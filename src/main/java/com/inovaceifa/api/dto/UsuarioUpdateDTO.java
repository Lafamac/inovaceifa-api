package com.inovaceifa.api.dto;

import lombok.Data;

@Data
public class UsuarioUpdateDTO {
    private String nome;
    private String email;
    private Integer perfilId;
}
