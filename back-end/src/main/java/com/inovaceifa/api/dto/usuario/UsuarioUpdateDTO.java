package com.inovaceifa.api.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioUpdateDTO {

    @Size(max = 100)
    private String nome;

    @Email(message = "Email inválido")
    @Size(max = 150)
    private String email;

    private Long perfilId;
}
