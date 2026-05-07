package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.segmentacao.*;
import com.inovaceifa.api.service.SegmentacaoFuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/segmentacao-funcionario")
@RequiredArgsConstructor
public class SegmentacaoFuncionarioController {

    private final SegmentacaoFuncionarioService service;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<SegmentacaoFuncionarioResponseDTO>> criar(
            @RequestBody SegmentacaoFuncionarioCreateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.criar(dto), "Criado com sucesso")
        );
    }

    @GetMapping("/{funcionarioId}")
    public ResponseEntity<ApiResponseDTO<List<SegmentacaoFuncionarioResponseDTO>>> listar(
            @PathVariable Long funcionarioId
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(funcionarioId), "Lista")
        );
    }
}