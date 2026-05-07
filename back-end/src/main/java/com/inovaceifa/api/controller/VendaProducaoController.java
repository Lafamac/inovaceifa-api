package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.venda.*;
import com.inovaceifa.api.service.VendaProducaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaProducaoController {

    private final VendaProducaoService service;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<VendaProducaoResponseDTO>> criar(
            @RequestBody VendaProducaoCreateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.criar(dto), "Venda registrada")
        );
    }

    @GetMapping("/talhao/{id}")
    public ResponseEntity<ApiResponseDTO<List<VendaProducaoResponseDTO>>> listar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listarPorTalhao(id), "Vendas carregadas")
        );
    }
}