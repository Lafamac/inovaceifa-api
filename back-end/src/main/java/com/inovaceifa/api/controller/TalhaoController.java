package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.talhao.TalhaoCreateDTO;
import com.inovaceifa.api.dto.talhao.TalhaoResponseDTO;
import com.inovaceifa.api.dto.talhao.TalhaoUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.TalhaoService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/talhoes")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "🌾 Talhões", description = "Gestão de talhões da fazenda ativa")
public class TalhaoController {

    private final TalhaoService talhaoService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TalhaoResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {
        PageResponseDTO<TalhaoResponseDTO> response = talhaoService.listar(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(response, "Talhões listados com sucesso"));
    }

    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<TalhaoResponseDTO>>> listarInativos(
            @ParameterObject Pageable pageable
    ) {
        PageResponseDTO<TalhaoResponseDTO> response = talhaoService.listarInativos(pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(response, "Talhões inativos listados com sucesso"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TalhaoResponseDTO>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        talhaoService.buscarPorId(id),
                        "Talhão carregado com sucesso"
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TalhaoResponseDTO>> criar(
            @Valid @RequestBody TalhaoCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        talhaoService.criarTalhao(dto),
                        "Talhão criado com sucesso"
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TalhaoResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TalhaoUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        talhaoService.atualizar(id, dto),
                        "Talhão atualizado com sucesso"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {
        talhaoService.excluir(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Talhão inativado com sucesso"));
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {
        talhaoService.reativar(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Talhão reativado com sucesso"));
    }
}