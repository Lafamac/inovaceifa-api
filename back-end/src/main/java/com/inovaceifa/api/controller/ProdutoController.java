package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.produto.ProdutoResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.produto.ProdutoUpdateDTO;
import com.inovaceifa.api.dto.produto.ProdutoCreateDTO;
import com.inovaceifa.api.service.ProdutoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(
        name = "Produtos",
        description = "Controle de produtos"
)
@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    /* =========================================================
       LISTAR GERAL
       ========================================================= */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProdutoResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(produtoService.listar(pageable), "Produtos listados")
        );
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */
    @GetMapping("/ativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProdutoResponseDTO>>> listarAtivos(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(produtoService.listar(pageable), "Produtos ativos listados")
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */
    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProdutoResponseDTO>>> listarInativos(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(produtoService.listarInativos(pageable), "Produtos inativos listados")
        );
    }

    /* =========================================================
       BUSCAR POR ID
       ========================================================= */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProdutoResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(produtoService.buscarPorId(id), "Produto encontrado")
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProdutoResponseDTO>> criar(
            @Valid @RequestBody ProdutoCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(produtoService.criar(dto), "Produto criado"));
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProdutoResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(produtoService.atualizar(id, dto), "Produto atualizado")
        );
    }

    /* =========================================================
       DESATIVAR (SOFT DELETE)
       ========================================================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Produto desativado"));
    }

    /* =========================================================
       REATIVAR
       ========================================================= */
    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {
        produtoService.reativar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Produto reativado"));
    }
}