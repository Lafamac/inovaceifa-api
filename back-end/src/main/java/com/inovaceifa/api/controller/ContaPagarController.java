package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.contapagar.ContaPagarPagamentoDTO;
import com.inovaceifa.api.dto.contapagar.ContaPagarResponseDTO;
import com.inovaceifa.api.dto.contapagar.ContaPagarUpdateDTO;
import com.inovaceifa.api.dto.contapagar.ContaPagarCreateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.ContaPagarService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contas-pagar")
@RequiredArgsConstructor
@Tag(name = "💰 Financeiro", description = "Contas a pagar da fazenda e safra ativa")
@SecurityRequirement(name = "bearerAuth")
public class ContaPagarController {

    private final ContaPagarService contaPagarService;

    /* =========================================================
       LISTAR CONTAS A PAGAR
       ========================================================= */

    @Operation(
            summary = "Listar contas a pagar",
            description = "Lista todas as contas a pagar da fazenda e safra ativa (paginado)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de contas a pagar"),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ContaPagarResponseDTO>>> listar(
            @ParameterObject
            @PageableDefault(sort = "dataVencimento", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        PageResponseDTO<ContaPagarResponseDTO> response =
                contaPagarService.listar(pageable);

        String mensagem = response.getContent().isEmpty()
                ? "Nenhuma conta a pagar encontrada"
                : "Contas a pagar listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, mensagem));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ContaPagarResponseDTO>> criar(
            @Valid @RequestBody ContaPagarCreateDTO dto
    ) {

        ContaPagarResponseDTO response =
                contaPagarService.criar(dto);

        return ResponseEntity.status(201).body(
                ApiResponseDTO.success(
                        response,
                        "Conta a pagar criada com sucesso"
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ContaPagarResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContaPagarUpdateDTO dto
    ) {

        ContaPagarResponseDTO response =
                contaPagarService.atualizar(id, dto);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        response,
                        "Conta atualizada com sucesso"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(
            @PathVariable Long id
    ) {

        contaPagarService.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        null,
                        "Conta removida com sucesso"
                )
        );
    }

    @GetMapping("/fazenda")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ContaPagarResponseDTO>>> listarFazenda(
            @ParameterObject
            @PageableDefault(sort = "dataVencimento", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        PageResponseDTO<ContaPagarResponseDTO> response =
                contaPagarService.listarFazenda(pageable);

        String mensagem = response.getContent().isEmpty()
                ? "Nenhuma conta encontrada"
                : "Contas da fazenda listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, mensagem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ContaPagarResponseDTO>> buscar(
            @PathVariable Long id
    ) {

        ContaPagarResponseDTO response =
                contaPagarService.buscar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        response,
                        "Conta encontrada com sucesso"
                )
        );
    }

    /* =========================================================
       PAGAR CONTA
       ========================================================= */

    @Operation(
            summary = "Pagar conta",
            description = "Realiza o pagamento de uma conta a pagar da fazenda ativa."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conta paga",
                    content = @Content(schema = @Schema(implementation = ContaPagarResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "404", description = "Conta não encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class))),
            @ApiResponse(responseCode = "409", description = "Conta já paga",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PostMapping("/{id}/pagar")
    public ResponseEntity<ApiResponseDTO<ContaPagarResponseDTO>> pagar(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ContaPagarPagamentoDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo",
                                    value = """
                                    {
                                      "dataPagamento": "2025-03-15",
                                      "vlrJuros": 25.50
                                    }
                                    """
                            )
                    )
            )
            @Valid @RequestBody ContaPagarPagamentoDTO dto
    ) {

        ContaPagarResponseDTO response =
                contaPagarService.pagar(id, dto);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        response,
                        "Conta paga com sucesso"
                )
        );
    }
}
