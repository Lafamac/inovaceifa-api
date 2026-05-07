package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.horamaquina.HoraMaquinaResponseDTO;
import com.inovaceifa.api.dto.horamaquina.HoraMaquinaCreateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.HoraMaquinaService;
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
        name = "🚜 Máquinas",
        description = "Controle de horas de uso das máquinas"
)
@RestController
@RequestMapping("/horas-maquina")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class HoraMaquinaController {

    private final HoraMaquinaService horaMaquinaService;

    /* =========================================================
       LISTAR
       ========================================================= */

    @Operation(
            summary = "Listar horas de máquina",
            description = "Lista todas as horas registradas das máquinas da fazenda e safra ativas (paginado)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada de horas"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<HoraMaquinaResponseDTO>>> listar(
            @ParameterObject
            @PageableDefault(sort = "dataExecucao", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {

        PageResponseDTO<HoraMaquinaResponseDTO> response =
                horaMaquinaService.listar(pageable);

        String mensagem = response.getContent().isEmpty()
                ? "Nenhuma hora de máquina registrada"
                : "Horas de máquina listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, mensagem));
    }

    /* =========================================================
   ATUALIZAR (NOVO)
   ========================================================= */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<HoraMaquinaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody HoraMaquinaCreateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        horaMaquinaService.atualizar(id, dto),
                        "Horas de máquina atualizadas com sucesso"
                )
        );
    }

    /* =========================================================
       EXCLUIR (NOVO)
       ========================================================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {

        horaMaquinaService.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Hora de máquina excluída com sucesso")
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Operation(
            summary = "Registrar horas de máquina",
            description = "Registra horas de uso de uma máquina em uma data específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Horas registradas",
                    content = @Content(schema = @Schema(implementation = HoraMaquinaResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "404", description = "Máquina não encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<HoraMaquinaResponseDTO>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = HoraMaquinaCreateDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "maquinaId": 3,
                                      "dataExecucao": "2025-01-10",
                                      "horasTrabalhadas": 8.5,
                                      "descricao": "Pulverização de talhão 12"
                                    }
                                    """
                            )
                    )
            )
            @Valid @RequestBody HoraMaquinaCreateDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponseDTO.success(
                                horaMaquinaService.criar(dto),
                                "Horas de máquina registradas com sucesso"
                        )
                );
    }
}
