package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.terceirizado.*;
import com.inovaceifa.api.service.TerceirizadoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "Terceirizados",
        description = "Controle terceirizados"
)
@RestController
@RequestMapping("/terceirizados")
@RequiredArgsConstructor
public class TerceirizadoController {

    private final TerceirizadoService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TerceirizadoResponseDTO>>> listar(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(pageable),
                        "Terceirizados listados com sucesso"));
    }

    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TerceirizadoResponseDTO>>> listarInativos(
            @ParameterObject Pageable pageable) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listarInativos(pageable),
                        "Terceirizados inativos listados com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TerceirizadoResponseDTO>> buscar(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.buscar(id),
                        "Terceirizado carregado com sucesso"));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TerceirizadoResponseDTO>> criar(
            @Valid @RequestBody TerceirizadoCreateDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(service.criar(dto),
                        "Terceirizado criado com sucesso"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TerceirizadoResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TerceirizadoUpdateDTO dto) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.atualizar(id, dto),
                        "Terceirizado atualizado com sucesso"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Terceirizado inativado com sucesso"));
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {

        service.reativar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Terceirizado reativado com sucesso"));
    }
}