package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.auditoria.AuditoriaDiffDTO;
import com.inovaceifa.api.service.AuditoriaDiffService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens-servico")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "🧾 Auditoria Avançada")
public class AuditoriaDiffController {

    private final AuditoriaDiffService service;

    @GetMapping("/{id}/auditoria/detalhe")
    public ResponseEntity<ApiResponseDTO<List<AuditoriaDiffDTO>>> historico(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.listarDiffPorOrdem(id),
                        "Histórico detalhado carregado"
                )
        );
    }
}