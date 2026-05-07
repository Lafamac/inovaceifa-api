package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.folhapagamento.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.FolhaPagamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/folha-pagamento")
@RequiredArgsConstructor
@Tag(name = "💰 Folha de Pagamento")
public class FolhaPagamentoController {

    private final FolhaPagamentoService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<FolhaPagamentoResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(pageable), "Lista de salários")
        );
    }

    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<FolhaPagamentoResponseDTO>>> listarInativos(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listarInativos(pageable), "Inativos")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FolhaPagamentoResponseDTO>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.buscar(id), "Registro encontrado")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<FolhaPagamentoResponseDTO>> criar(
            @RequestBody FolhaPagamentoCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(service.criar(dto), "Criado com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FolhaPagamentoResponseDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody FolhaPagamentoUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.atualizar(id, dto), "Atualizado com sucesso")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Inativado"));
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {
        service.reativar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Reativado"));
    }
}