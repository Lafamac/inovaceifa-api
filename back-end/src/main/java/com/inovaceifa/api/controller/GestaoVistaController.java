package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.relatorio.GestaoVistaResponseDTO;
import com.inovaceifa.api.service.GestaoVistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class GestaoVistaController {

    private final GestaoVistaService service;

    @GetMapping("/gestao-vista")
    public ResponseEntity<ApiResponseDTO<GestaoVistaResponseDTO>> listar() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.listar(),
                        "Gestão à vista carregada"
                )
        );
    }
}