package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.MeResponseDTO;
import com.inovaceifa.api.dto.login.LoginRequestDTO;
import com.inovaceifa.api.dto.login.LoginResponseDTO;
import com.inovaceifa.api.dto.login.TrocarSenhaDTO;
import com.inovaceifa.api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "🔐 Autenticação", description = "Login, token JWT e dados do usuário autenticado")
public class AuthController {

    private final AuthService authService;

    /* =========================================================
       LOGIN
       ========================================================= */

    @Operation(
            summary = "Login do usuário",
            description = "Autentica o usuário e retorna um token JWT",
            security = {}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = LoginResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Usuário ou senha inválidos",
                    content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Erro interno do servidor"
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO dto
    ) {

        LoginResponseDTO response = authService.login(dto);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        response,
                        "Login realizado com sucesso"
                )
        );
    }

    /* =========================================================
       /ME
       ========================================================= */

    @Operation(
            summary = "Dados do usuário autenticado",
            description = "Retorna o contexto do usuário logado (perfil, fazenda, safra, etc).",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuário autenticado",
            content = @Content(schema = @Schema(implementation = MeResponseDTO.class))
    )
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<MeResponseDTO>> me() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        authService.me(),
                        "Contexto do usuário carregado"
                )
        );
    }

    /* =========================================================
       TROCAR SENHA
       ========================================================= */

    @Operation(
            summary = "Trocar senha do usuário",
            description = "Permite ao usuário autenticado alterar sua senha",
            security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Senha alterada com sucesso",
            content = @Content(schema = @Schema(implementation = ApiResponseDTO.class))
    )
    @PutMapping("/trocar-senha")
    public ResponseEntity<ApiResponseDTO<Void>> trocarSenha(
            @Valid @RequestBody TrocarSenhaDTO dto
    ) {

        authService.trocarSenha(dto);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        null,
                        "Senha alterada com sucesso"
                )
        );
    }
}
