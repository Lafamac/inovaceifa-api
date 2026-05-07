package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.auditoria.AuditoriaOrdemServicoResponseDTO;
import com.inovaceifa.api.service.AuditoriaConsultaService;
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
@Tag(name = "🧾 Auditoria", description = "Histórico de alterações da ordem de serviço")
public class AuditoriaOrdemServicoController {

    private final AuditoriaConsultaService service;

    @GetMapping("/{ordemId}/auditoria")
    public ResponseEntity<ApiResponseDTO<List<AuditoriaOrdemServicoResponseDTO>>> listar(
            @PathVariable Long ordemId
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.listarPorOrdem(ordemId),
                        "Histórico carregado com sucesso"
                )
        );
    }
}