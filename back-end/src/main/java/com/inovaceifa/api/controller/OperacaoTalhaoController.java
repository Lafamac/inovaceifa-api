package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.operacaotalhao.OperacaoTalhaoCreateDTO;
import com.inovaceifa.api.dto.operacaotalhao.OperacaoTalhaoResponseDTO;
import com.inovaceifa.api.dto.operacaotalhao.OperacaoTalhaoUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.OperacaoTalhaoService;
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

@RestController
@RequestMapping("/operacoes-talhao")
@RequiredArgsConstructor
@Tag(name = "🌱 Operações de Talhão", description = "Controle das operações executadas em cada talhão")
@SecurityRequirement(name = "bearerAuth")
public class OperacaoTalhaoController {

    private final OperacaoTalhaoService service;

    /* =========================================================
       LISTAR
       ========================================================= */

    @Operation(
            summary = "Listar operações de talhão",
            description = "Lista todas as operações registradas para o proprietário, fazenda e safra ativos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada retornada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<OperacaoTalhaoResponseDTO>>> listar(
            @ParameterObject
            @PageableDefault(sort = "dataExecucao", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {

        PageResponseDTO<OperacaoTalhaoResponseDTO> response = service.listar(pageable);

        String mensagem = response.getContent().isEmpty()
                ? "Nenhuma operação de talhão registrada"
                : "Operações de talhão listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, mensagem));
    }

    /* =========================================================
       BUSCAR POR ID
       ========================================================= */

    @Operation(summary = "Buscar operação de talhão por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OperacaoTalhaoResponseDTO>> buscar(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.buscarPorId(id),
                        "Operação de talhão carregada com sucesso"
                )
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Operation(summary = "Registrar operação em talhão")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Operação registrada",
                    content = @Content(schema = @Schema(implementation = OperacaoTalhaoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<OperacaoTalhaoResponseDTO>> criar(
            @Valid @RequestBody OperacaoTalhaoCreateDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        service.criar(dto),
                        "Operação de talhão registrada com sucesso"
                ));
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @Operation(summary = "Atualizar operação de talhão")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OperacaoTalhaoResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OperacaoTalhaoUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.atualizar(id, dto),
                        "Operação de talhão atualizada com sucesso"
                )
        );
    }

    /* =========================================================
       EXCLUIR
       ========================================================= */

    @Operation(summary = "Excluir operação de talhão")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Operação de talhão excluída com sucesso")
        );
    }
}