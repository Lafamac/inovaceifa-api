package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.apontamento.ApontamentoTurmaCreateDTO;
import com.inovaceifa.api.dto.apontamento.ApontamentoTurmaResponseDTO;
import com.inovaceifa.api.service.ApontamentoTurmaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Apontamento Turma",
        description = "Apontamento das turmas"
)
@RestController
@RequestMapping("/apontamentos-turma")
@RequiredArgsConstructor
public class ApontamentoTurmaController {

    private final ApontamentoTurmaService service;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ApontamentoTurmaResponseDTO>> registrar(
            @Valid @RequestBody ApontamentoTurmaCreateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.registrar(dto),
                        "Apontamento de turma registrado com sucesso"
                )
        );
    }

    /* =========================
       🔥 NOVO ENDPOINT
       ========================= */

    @GetMapping("/ordem-servico/{id}")
    public ResponseEntity<ApiResponseDTO<List<ApontamentoTurmaResponseDTO>>> listarPorOs(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.listarPorOs(id),
                        "OK"
                )
        );
    }

    /* (mantido se já existe) */
    @GetMapping("/por-fazenda-safra")
    public ResponseEntity<ApiResponseDTO<List<ApontamentoTurmaResponseDTO>>> listarPorSafra() {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listarPorSafra(), "OK")
        );
    }
}