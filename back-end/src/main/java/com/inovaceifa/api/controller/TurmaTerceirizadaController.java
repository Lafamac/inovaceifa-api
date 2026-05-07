package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.turma.*;
import com.inovaceifa.api.service.TurmaTerceirizadaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(
        name = " Turmas terceirizadas",
        description = "Controle de turmas terceirizadas"
)
@RestController
@RequestMapping("/turmas-terceirizadas")
@RequiredArgsConstructor
public class TurmaTerceirizadaController {

    private final TurmaTerceirizadaService service;

    /* =========================================================
       LISTAR ATIVAS
       ========================================================= */

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TurmaResponseDTO>>> listar(
            @ParameterObject
            @PageableDefault(sort = "nome", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        var response = service.listar(pageable);

        String msg = response.getContent().isEmpty()
                ? "Nenhuma turma terceirizada cadastrada"
                : "Turmas terceirizadas listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, msg));
    }

    /* =========================================================
       LISTAR INATIVAS
       ========================================================= */

    @GetMapping("/inativas")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TurmaResponseDTO>>> listarInativas(
            @ParameterObject
            @PageableDefault(sort = "nome", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        var response = service.listarInativas(pageable);

        String msg = response.getContent().isEmpty()
                ? "Nenhuma turma terceirizada inativa encontrada"
                : "Turmas terceirizadas inativas listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, msg));
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TurmaResponseDTO>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.success(service.buscar(id), "Turma carregada"));
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TurmaResponseDTO>> criar(@Valid @RequestBody TurmaCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(service.criar(dto), "Turma criada"));
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TurmaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TurmaUpdateDTO dto) {

        return ResponseEntity.ok(ApiResponseDTO.success(service.atualizar(id, dto), "Turma atualizada"));
    }

    /* =========================================================
       DESATIVAR
       ========================================================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {

        service.excluir(id);

        return ResponseEntity.ok(ApiResponseDTO.success(null, "Turma desativada"));
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {

        service.reativar(id);

        return ResponseEntity.ok(ApiResponseDTO.success(null, "Turma reativada"));
    }
}