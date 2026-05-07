package com.inovaceifa.api.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "LoginRequest", description = "Credenciais do usuário para autenticação")
public class LoginRequestDTO {

    @Schema(
            description = "Email do usuário",
            example = "admin@teste.com"
    )
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @Schema(
            description = "Senha do usuário",
            example = "123456"
    )
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
}
