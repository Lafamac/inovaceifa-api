package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.pedidocompra.*;
import com.inovaceifa.api.service.PedidoCompraService;
import com.inovaceifa.api.swagger.ApiErrorSchema;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@RequestMapping("/pedidos-compra")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(
        name = "🛒 Pedido de Compra",
        description = "Gestão completa de pedidos de compra (ativo/inativo, aprovação, recebimento)"
)
public class PedidoCompraController {

    private final PedidoCompraService service;

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    @Operation(summary = "Listar pedidos ATIVOS")
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<PedidoCompraResponseDTO>>> listar(
            @Parameter(hidden = true)
            @ParameterObject
            @PageableDefault(sort = "data", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.listar(pageable),
                        "Pedidos ativos listados com sucesso"
                )
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    @Operation(summary = "Listar pedidos INATIVOS")
    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<PedidoCompraResponseDTO>>> listarInativos(
            @Parameter(hidden = true)
            @ParameterObject
            @PageableDefault(sort = "data", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.listarInativos(pageable),
                        "Pedidos inativos listados com sucesso"
                )
        );
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    @Operation(summary = "Buscar pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PedidoCompraResponseDTO>> buscar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.buscar(id),
                        "Pedido encontrado"
                )
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Operation(summary = "Criar pedido")
    @PostMapping
    public ResponseEntity<ApiResponseDTO<PedidoCompraResponseDTO>> criar(
            @Valid @RequestBody PedidoCompraCreateDTO dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        service.criar(dto),
                        "Pedido criado com sucesso"
                ));
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @Operation(summary = "Atualizar pedido")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PedidoCompraResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoCompraUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.atualizar(id, dto),
                        "Pedido atualizado com sucesso"
                )
        );
    }

    /* =========================================================
       🔥 INATIVAR (SOFT DELETE)
       ========================================================= */

    @Operation(summary = "Inativar pedido")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> inativar(
            @PathVariable Long id
    ) {

        service.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        null,
                        "Pedido inativado com sucesso"
                )
        );
    }

    /* =========================================================
       🔥 REATIVAR
       ========================================================= */

    @Operation(summary = "Reativar pedido")
    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(
            @PathVariable Long id
    ) {

        service.reativar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        null,
                        "Pedido reativado com sucesso"
                )
        );
    }

    /* =========================================================
       APROVAR
       ========================================================= */

    @Operation(summary = "Aprovar pedido")
    @PostMapping("/{id}/aprovar")
    public ResponseEntity<ApiResponseDTO<Void>> aprovar(
            @PathVariable Long id
    ) {

        service.aprovar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        null,
                        "Pedido aprovado com sucesso"
                )
        );
    }

    /* =========================================================
       RECEBER
       ========================================================= */

    @Operation(summary = "Receber pedido (gera estoque + financeiro)")
    @PostMapping("/{id}/receber")
    public ResponseEntity<ApiResponseDTO<Void>> receber(
            @PathVariable Long id
    ) {

        service.receber(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        null,
                        "Pedido recebido com sucesso"
                )
        );
    }
}