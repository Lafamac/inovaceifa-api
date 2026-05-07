package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.operacaoproduto.OperacaoProdutoCreateDTO;
import com.inovaceifa.api.dto.operacaoproduto.OperacaoProdutoResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.OperacaoProdutoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "Operacção Produto",
        description = "Controle de uso de produtos"
)
@RestController
@RequestMapping("/operacao-produtos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class OperacaoProdutoController {

    private final OperacaoProdutoService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<OperacaoProdutoResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(pageable), "Produtos da operação listados")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<OperacaoProdutoResponseDTO>> criar(
            @Valid @RequestBody OperacaoProdutoCreateDTO dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(service.criar(dto), "Produto aplicado registrado"));
    }
}