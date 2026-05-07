package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.referencia.*;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
public abstract class ReferenciaBaseController {

    protected final ReferenciaBaseService<?> service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ReferenciaResponseDTO>>> listarAtivos() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listarAtivos(), "Registros carregados")
        );
    }

    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaResponseDTO>>> listarInativos() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listarInativos(), "Registros inativos")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ReferenciaResponseDTO>> buscar(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.buscar(id), "Registro encontrado")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ReferenciaResponseDTO>> criar(
            @Valid @RequestBody ReferenciaCreateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.criar(dto), "Registro criado")
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ReferenciaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReferenciaUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.atualizar(id, dto), "Registro atualizado")
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> desativar(@PathVariable Long id) {

        service.desativar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Registro desativado")
        );
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {

        service.reativar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Registro reativado")
        );
    }
}