package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.gastomaquina.GastoMaquinaCreateDTO;
import com.inovaceifa.api.dto.gastomaquina.GastoMaquinaResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.GastoMaquinaService;
import com.inovaceifa.api.swagger.ApiErrorSchema;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
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
        name = "💰 Gastos de Máquina",
        description = "Controle de custos operacionais das máquinas da fazenda ativa"
)
@RestController
@RequestMapping("/gastos-maquina")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class GastoMaquinaController {

    private final GastoMaquinaService gastoMaquinaService;

    /* =========================================================
       LISTAR
       ========================================================= */

    @Operation(
            summary = "Listar gastos de máquina",
            description = "Retorna todos os gastos das máquinas da fazenda e safra ativas (paginado)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada de gastos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<GastoMaquinaResponseDTO>>> listar(
            @ParameterObject
            @PageableDefault(sort = "data", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {

        PageResponseDTO<GastoMaquinaResponseDTO> response =
                gastoMaquinaService.listar(pageable);

        String msg = response.getContent().isEmpty()
                ? "Nenhum gasto de máquina encontrado"
                : "Gastos de máquina listados com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, msg));
    }

    /* =========================================================
   ATUALIZAR (NOVO)
   ========================================================= */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<GastoMaquinaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody GastoMaquinaCreateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        gastoMaquinaService.atualizar(id, dto),
                        "Gasto de máquina atualizado com sucesso"
                )
        );
    }

    /* =========================================================
       EXCLUIR (NOVO)
       ========================================================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {

        gastoMaquinaService.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Gasto de máquina excluído com sucesso")
        );
    }


    /* =========================================================
       CRIAR
       ========================================================= */

    @Operation(
            summary = "Registrar gasto de máquina",
            description = """
                    Registra um gasto operacional de máquina.
                    
                    Exemplos:
                    - Combustível
                    - Manutenção
                    - Lubrificante
                    - Peças
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Gasto registrado",
                    content = @Content(schema = @Schema(implementation = GastoMaquinaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "404", description = "Máquina ou tipo de gasto não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<GastoMaquinaResponseDTO>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GastoMaquinaCreateDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "maquinaId": 3,
                                      "tipoGastoId": 2,
                                      "data": "2025-01-10",
                                      "descricao": "Troca de óleo",
                                      "valor": 350.75
                                    }
                                    """
                            )
                    )
            )
            @Valid @RequestBody GastoMaquinaCreateDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseDTO.success(
                                gastoMaquinaService.criar(dto),
                                "Gasto de máquina criado com sucesso"
                        )
                );
    }
}
