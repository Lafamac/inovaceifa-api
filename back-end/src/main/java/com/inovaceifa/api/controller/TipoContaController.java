package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.tipoconta.*;
import com.inovaceifa.api.service.TipoContaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "Tipo Conta",
        description = "Tabela de referência de tipo de conta"
)
@RestController
@RequestMapping("/tipo-conta")
@RequiredArgsConstructor
public class TipoContaController {

    private final TipoContaService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TipoContaResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(pageable), "Tipos de conta listados")
        );
    }

    @GetMapping("/ativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TipoContaResponseDTO>>> listarAtivos(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listarAtivos(pageable), "Tipos de conta ativos")
        );
    }

    @GetMapping("/buscar-arvore")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TipoContaResponseDTO>>> buscarPorArvore(
            @RequestParam String arvore,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.buscarPorArvore(arvore, pageable), "Busca realizada")
        );
    }

    @GetMapping("/buscar-indice")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TipoContaResponseDTO>>> buscarPorIndice(
            @RequestParam String indice,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.buscarPorIndice(indice, pageable), "Busca realizada")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TipoContaResponseDTO>> criar(
            @RequestBody TipoContaCreateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.criar(dto), "Tipo de conta criado")
        );
    }

    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TipoContaResponseDTO>>> listarInativos(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listarInativos(pageable), "Tipos de conta inativos")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TipoContaResponseDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody TipoContaUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.atualizar(id, dto), "Tipo de conta atualizado")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> desativar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Tipo de conta desativado"));
    }
}