package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.administrativo.AdministrativoCreateDTO;
import com.inovaceifa.api.dto.administrativo.AdministrativoUpdateDTO;
import com.inovaceifa.api.dto.administrativo.AdministrativoResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.AdministrativoService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "📊 Administrativo",
        description = "Custos administrativos por safra e fazenda"
)
@RestController
@RequestMapping("/administrativo")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AdministrativoController {

    private final AdministrativoService administrativoService;

    /* =========================================================
       LISTAR (FAZENDA + SAFRA ATIVAS)
       ========================================================= */

    @Operation(
            summary = "Listar custos administrativos",
            description = "Lista todos os custos administrativos da fazenda e safra ativas (paginado)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada de registros administrativos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<AdministrativoResponseDTO>>> listar(
            @ParameterObject
            @PageableDefault(sort = "descricao", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        PageResponseDTO<AdministrativoResponseDTO> page =
                administrativoService.listar(pageable);

        String mensagem = page.getContent().isEmpty()
                ? "Nenhum registro administrativo encontrado"
                : "Administrativo listado com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(page, mensagem));
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Operation(
            summary = "Criar custo administrativo",
            description = "Cria um novo custo administrativo para a fazenda e safra ativas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registro administrativo criado",
                    content = @Content(schema = @Schema(implementation = AdministrativoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "409", description = "Registro administrativo já existe",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<AdministrativoResponseDTO>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AdministrativoCreateDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "descricao": "Energia elétrica escritório",
                                      "valor": 2500.00,
                                      "data": "2025-01-10",
                                      "contaGerencialId": 3
                                    }
                                    """
                            )
                    )
            )
            @Valid @RequestBody AdministrativoCreateDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseDTO.success(
                                administrativoService.criar(dto),
                                "Registro administrativo criado com sucesso"
                        )
                );
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @Operation(
            summary = "Atualizar custo administrativo",
            description = "Atualiza um registro administrativo existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registro administrativo atualizado",
                    content = @Content(schema = @Schema(implementation = AdministrativoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "404", description = "Registro administrativo não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<AdministrativoResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AdministrativoUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        administrativoService.atualizar(id, dto),
                        "Registro administrativo atualizado com sucesso"
                )
        );
    }
}
