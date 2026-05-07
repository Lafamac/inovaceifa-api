package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoMaquinaCreateDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoMaquinaResponseDTO;
import com.inovaceifa.api.service.PlanejamentoMaquinaService;
import com.inovaceifa.api.swagger.ApiErrorSchema;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "🚜 Planejamento Máquinas", description = "Gestão de máquinas no planejamento da operação")
public class PlanejamentoMaquinaController {

    private final PlanejamentoMaquinaService service;

    @PostMapping("/{id}/maquinas")
    public ResponseEntity<ApiResponseDTO<PlanejamentoMaquinaResponseDTO>> adicionar(
            @PathVariable Long id,
            @Valid @RequestBody PlanejamentoMaquinaCreateDTO dto
    ) {

        PlanejamentoMaquinaResponseDTO response = service.adicionar(id, dto);

        return ResponseEntity.ok(
                ApiResponseDTO.success(response, "Máquina adicionada ao planejamento com sucesso")
        );
    }

    @GetMapping("/{id}/maquinas")
    public ResponseEntity<ApiResponseDTO<List<PlanejamentoMaquinaResponseDTO>>> listar(
            @PathVariable Long id
    ) {

        List<PlanejamentoMaquinaResponseDTO> response = service.listar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(response, "Máquinas do planejamento listadas com sucesso")
        );
    }

    @DeleteMapping("/{id}/maquinas/{itemId}")
    public ResponseEntity<ApiResponseDTO<Void>> remover(
            @PathVariable Long id,
            @PathVariable Long itemId
    ) {
        service.removerMaquina(id, itemId);
        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Máquina removida com sucesso")
        );
    }
}