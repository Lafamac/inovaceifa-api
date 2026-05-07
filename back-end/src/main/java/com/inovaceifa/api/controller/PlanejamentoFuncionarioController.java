package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoFuncionarioCreateDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoFuncionarioResponseDTO;
import com.inovaceifa.api.service.PlanejamentoFuncionarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planejamento-operacao")
@RequiredArgsConstructor
@Tag(name = "📋 Planejamento Funcionários", description = "Planejamento de mão de obra")
@SecurityRequirement(name = "bearerAuth")
public class PlanejamentoFuncionarioController {

    private final PlanejamentoFuncionarioService service;

    /* ========================= CRIAR ========================= */

    @PostMapping("/{id}/funcionarios")
    public ResponseEntity<ApiResponseDTO<PlanejamentoFuncionarioResponseDTO>> criar(
            @PathVariable Long id,
            @Valid @RequestBody PlanejamentoFuncionarioCreateDTO dto
    ) {

        PlanejamentoFuncionarioResponseDTO response = service.criar(id, dto);

        return ResponseEntity.ok(
                ApiResponseDTO.success(response, "Funcionário planejado com sucesso")
        );
    }

    /* ========================= LISTAR ========================= */

    @GetMapping("/{id}/funcionarios")
    public ResponseEntity<ApiResponseDTO<List<PlanejamentoFuncionarioResponseDTO>>> listar(
            @PathVariable Long id
    ) {

        List<PlanejamentoFuncionarioResponseDTO> response = service.listar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(response, "Funcionários listados com sucesso")
        );
    }

    /* ========================= REMOVER ========================= */

    @DeleteMapping("/{id}/funcionarios/{itemId}")
    public ResponseEntity<ApiResponseDTO<Void>> remover(
            @PathVariable Long id,
            @PathVariable Long itemId
    ) {
        service.remover(id, itemId);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Funcionário removido com sucesso")
        );
    }
}