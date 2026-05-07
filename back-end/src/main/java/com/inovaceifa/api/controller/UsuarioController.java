package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.usuario.UsuarioCreateDTO;
import com.inovaceifa.api.dto.usuario.UsuarioResponseDTO;
import com.inovaceifa.api.dto.usuario.UsuarioUpdateDTO;
import com.inovaceifa.api.service.UsuarioService;
import com.inovaceifa.api.swagger.ApiErrorSchema;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "👥 Usuários", description = "Gestão de usuários do sistema (somente SUPER USUÁRIO)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /* =========================================================
       LISTAR
       ========================================================= */

    @Operation(
            summary = "Listar usuários",
            description = "Lista todos os usuários do sistema (somente SUPER USUÁRIO)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada de usuários"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<UsuarioResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {

        PageResponseDTO<UsuarioResponseDTO> response =
                usuarioService.listar(pageable);

        String mensagem = response.getContent().isEmpty()
                ? "Nenhum usuário encontrado"
                : "Usuários listados com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, mensagem));
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Operation(
            summary = "Criar usuário",
            description = "Cria um novo usuário no sistema (somente SUPER USUÁRIO)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = UsuarioCreateDTO.class))
            )
            @Valid @RequestBody UsuarioCreateDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        usuarioService.criar(dto),
                        "Usuário criado com sucesso"
                ));
    }

    /* =========================================================
       BUSCAR POR ID
       ========================================================= */

    @Operation(
            summary = "Buscar usuário",
            description = "Retorna os dados de um usuário pelo ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> buscar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        usuarioService.buscarPorId(id),
                        "Usuário carregado com sucesso"
                )
        );
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @Operation(
            summary = "Atualizar usuário",
            description = "Atualiza os dados de um usuário existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado",
                    content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UsuarioResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        usuarioService.atualizar(id, dto),
                        "Usuário atualizado com sucesso"
                )
        );
    }

    /* =========================================================
       EXCLUIR
       ========================================================= */

    @Operation(
            summary = "Excluir usuário",
            description = "Remove um usuário do sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário excluído"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(
            @PathVariable Long id
    ) {

        usuarioService.deletar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Usuário excluído com sucesso")
        );
    }
}
