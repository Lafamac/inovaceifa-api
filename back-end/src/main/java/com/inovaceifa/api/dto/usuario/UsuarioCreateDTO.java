package com.inovaceifa.api.dto.usuario;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UsuarioCreateDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 150)
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    @NotNull(message = "Perfil é obrigatório")
    private Long perfilId;
}
