package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.safra.SafraCreateDTO;
import com.inovaceifa.api.dto.safra.SafraResponseDTO;
import com.inovaceifa.api.service.SafraService;
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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/safras")
@RequiredArgsConstructor
@Tag(name = "🌱 Safras", description = "Gestão de safras da fazenda ativa")
@SecurityRequirement(name = "bearerAuth")
public class SafraController {

    private final SafraService safraService;

    /* =========================================================
       LISTAR
       ========================================================= */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<SafraResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {

        PageResponseDTO<SafraResponseDTO> response =
                safraService.listar(pageable);

        String msg = response.getContent().isEmpty()
                ? "Nenhuma safra cadastrada"
                : "Safras listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, msg));
    }

    /* =========================================================
       BUSCAR POR ID
       ========================================================= */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<SafraResponseDTO>> buscar(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        safraService.buscarPorId(id),
                        "Safra carregada com sucesso"
                )
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<SafraResponseDTO>> criar(
            @Valid @RequestBody SafraCreateDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        safraService.criarSafra(dto),
                        "Safra criada com sucesso"
                ));
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<SafraResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody SafraCreateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        safraService.atualizar(id, dto),
                        "Safra atualizada com sucesso"
                )
        );
    }

    /* =========================================================
       EXCLUIR
       ========================================================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {

        safraService.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Safra excluída com sucesso")
        );
    }
}
