package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.proprietario.*;
import com.inovaceifa.api.service.ProprietarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springdoc.core.annotations.ParameterObject;

@Tag(
        name = "Proprietários",
        description = "Controle de proprietários"
)
@RestController
@RequestMapping("/proprietarios")
@RequiredArgsConstructor
public class ProprietarioController {

    private final ProprietarioService service;

    /* =========================================================
       LISTAR GERAL
       ========================================================= */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProprietarioResponseDTO>>> listar(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiResponseDTO.success(service.listar(pageable), "Lista geral"));
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */
    @GetMapping("/ativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProprietarioResponseDTO>>> ativos(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiResponseDTO.success(service.listarAtivos(pageable), "Lista de ativos"));
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */
    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProprietarioResponseDTO>>> inativos(
            @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiResponseDTO.success(service.listarInativos(pageable), "Lista de inativos"));
    }

    /* =========================================================
       NOVO — BUSCAR POR ID
       ========================================================= */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProprietarioResponseDTO>> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.success(service.buscarPorId(id), "Proprietário encontrado"));
    }

    /* =========================================================
       NOVO — CRIAR
       ========================================================= */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProprietarioResponseDTO>> criar(
            @Valid @RequestBody ProprietarioCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(service.criar(dto), "Proprietário criado com sucesso"));
    }

    /* =========================================================
       NOVO — ATUALIZAR
       ========================================================= */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProprietarioResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProprietarioUpdateDTO dto) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.atualizar(id, dto), "Proprietário atualizado com sucesso"));
    }

    /* =========================================================
       SOFT DELETE
       ========================================================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Proprietário desativado"));
    }

    /* =========================================================
       REATIVAR
       ========================================================= */
    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {
        service.reativar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Proprietário reativado"));
    }
}