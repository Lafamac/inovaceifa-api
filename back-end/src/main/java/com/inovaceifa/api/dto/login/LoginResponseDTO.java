package com.inovaceifa.api.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(name = "LoginResponse", description = "Resposta do login com token JWT e dados do usuário")
public class LoginResponseDTO {

    @Schema(
            description = "ID do usuário autenticado",
            example = "15"
    )
    private Long id;

    @Schema(
            description = "Nome do usuário",
            example = "Administrador"
    )
    private String nome;

    @Schema(
            description = "Email do usuário",
            example = "admin@inovaceifa.com"
    )
    private String email;

    @Schema(
            description = "ID do perfil do usuário (ex: 1=Admin, 2=Super, 3=Funcionário)",
            example = "2"
    )
    private Long perfilId;

    @Schema(
            description = "Token JWT para autenticação nas requisições",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private String token;
}
