package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.relatorio.ComparacaoCompletaResponseDTO;
import com.inovaceifa.api.dto.relatorio.ComparacaoTalhaoResponseDTO;
import com.inovaceifa.api.service.ComparacaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "📊 Comparação Completa")
public class ComparacaoController {

    private final ComparacaoService service;

    @GetMapping("/completa")
    public ResponseEntity<ApiResponseDTO<ComparacaoCompletaResponseDTO>> comparar() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.compararCompleto(),
                        "Comparação completa gerada com sucesso"
                )
        );
    }

    @GetMapping("/por-talhao")
    public ResponseEntity<ApiResponseDTO<List<ComparacaoTalhaoResponseDTO>>> compararPorTalhao() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.compararPorTalhao(),
                        "Comparação por talhão gerada com sucesso"
                )
        );
    }
}