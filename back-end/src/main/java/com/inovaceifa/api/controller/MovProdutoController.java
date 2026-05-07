package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.produto.MovProdutoResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.produto.MovProdutoRequestDTO;
import com.inovaceifa.api.service.MovProdutoService;
import com.inovaceifa.api.swagger.ApiErrorSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mov-produtos")
@RequiredArgsConstructor
@Tag(
        name = "📦 Movimentação de Produtos",
        description = "Controle de entradas, saídas e ajustes de estoque da fazenda ativa"
)
@SecurityRequirement(name = "bearerAuth")
public class MovProdutoController {

    private final MovProdutoService movProdutoService;

    /* =========================================================
       LISTAR
       ========================================================= */

    @Operation(
            summary = "Listar movimentações de produtos",
            description = "Retorna todas as movimentações de produtos da fazenda e safra ativas, com paginação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista paginada de movimentações"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<MovProdutoResponseDTO>>> listar(
            @Parameter(hidden = true)
            @PageableDefault(sort = "dataMovimento", direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable
    ) {

        PageResponseDTO<MovProdutoResponseDTO> response =
                movProdutoService.listar(pageable);

        String mensagem = response.getContent().isEmpty()
                ? "Nenhuma movimentação encontrada"
                : "Movimentações de produtos listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, mensagem));
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Operation(
            summary = "Criar movimentação de produto",
            description = """
                    Registra uma entrada, saída ou ajuste de produto no estoque da fazenda ativa.

                    Esta operação:
                    • Atualiza o estoque
                    • Recalcula o custo médio
                    • Pode gerar contas a pagar automaticamente
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimentação criada",
                    content = @Content(schema = @Schema(implementation = MovProdutoResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "404", description = "Produto ou tipo de movimentação não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "409", description = "Estoque insuficiente ou conflito de regra de negócio",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponseDTO<MovProdutoResponseDTO>> criar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(schema = @Schema(implementation = MovProdutoRequestDTO.class))
            )
            @RequestBody MovProdutoRequestDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        movProdutoService.criar(dto),
                        "Movimentação de produto criada com sucesso"
                ));
    }
}
