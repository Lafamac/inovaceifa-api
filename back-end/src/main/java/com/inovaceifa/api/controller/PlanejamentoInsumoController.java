package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoInsumoCreateDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoInsumoResponseDTO;
import com.inovaceifa.api.service.PlanejamentoInsumoService;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "🌱 Planejamento Insumos", description = "Gestão de insumos do planejamento")
public class PlanejamentoInsumoController {

    private final PlanejamentoInsumoService service;

    @PostMapping("/{id}/insumos")
    public ResponseEntity<ApiResponseDTO<PlanejamentoInsumoResponseDTO>> adicionar(
            @PathVariable Long id,
            @Valid @RequestBody PlanejamentoInsumoCreateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.adicionar(id, dto), "Insumo adicionado com sucesso")
        );
    }

    @GetMapping("/{id}/insumos")
    public ResponseEntity<ApiResponseDTO<List<PlanejamentoInsumoResponseDTO>>> listar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(id), "Insumos listados com sucesso")
        );
    }

    @DeleteMapping("/{id}/insumos/{itemId}")
    public ResponseEntity<ApiResponseDTO<Void>> remover(
            @PathVariable Long id,
            @PathVariable Long itemId
    ) {
        service.removerInsumo(id, itemId);
        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Insumo removido com sucesso")
        );
    }
}