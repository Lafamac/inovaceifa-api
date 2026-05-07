package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.dashboard.DashboardSafraResponseDTO;
import com.inovaceifa.api.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "📊 Dashboard da Safra")
public class DashboardController {

    private final DashboardService service;

    /* =========================================================
       🔥 DASHBOARD PRINCIPAL
       ========================================================= */

    @GetMapping("/safra/{safraId}")
    @Operation(
            summary = "Dashboard completo da safra",
            description = """
            Retorna todos os indicadores consolidados da safra:

            ✔ Custo total
            ✔ Custo por hectare
            ✔ Custo por saca
            ✔ Receita
            ✔ Lucro
            ✔ Área total
            ✔ Produção total
            ✔ Custos por categoria
            """
    )
    public ResponseEntity<ApiResponseDTO<DashboardSafraResponseDTO>> dashboard(
            @Parameter(description = "ID da safra", required = true)
            @PathVariable Long safraId
    ) {

        DashboardSafraResponseDTO response = service.gerar(safraId);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        response,
                        "Dashboard gerado com sucesso"
                )
        );
    }
}