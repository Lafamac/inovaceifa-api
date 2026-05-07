package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.safratalhao.SafraTalhaoCreateDTO;
import com.inovaceifa.api.dto.safratalhao.SafraTalhaoResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.safratalhao.SafraTalhaoUpdateDTO;
import com.inovaceifa.api.service.SafraTalhaoService;
import com.inovaceifa.api.swagger.ApiErrorSchema;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
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
        name = "🌱 Safra Talhoes",
        description = "Vinculação de talhões à safra ativa"
)
@RestController
@RequestMapping("/safra-talhoes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "🌱 Safra Talhão")
public class SafraTalhaoController {

    private final SafraTalhaoService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<SafraTalhaoResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponseDTO.success(
                service.listar(pageable),
                "Listagem realizada"
        ));
    }

    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<SafraTalhaoResponseDTO>>> listarInativos(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponseDTO.success(
                service.listarInativos(pageable),
                "Inativos listados"
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<SafraTalhaoResponseDTO>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponseDTO.success(
                service.buscarPorId(id),
                "Registro encontrado"
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<SafraTalhaoResponseDTO>> criar(
            @Valid @RequestBody SafraTalhaoCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        service.criar(dto),
                        "Criado com sucesso"
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<SafraTalhaoResponseDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody SafraTalhaoUpdateDTO dto
    ) {
        return ResponseEntity.ok(ApiResponseDTO.success(
                service.atualizar(id, dto),
                "Atualizado com sucesso"
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Inativado com sucesso"));
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {
        service.reativar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Reativado com sucesso"));
    }
}