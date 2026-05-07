package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.parametro.ParametroResponseDTO;
import com.inovaceifa.api.service.ParametroService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parametros")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ParametroController {

    private final ParametroService service;

    /* =========================================================
       🔥 ENCARGOS
       ========================================================= */

    @GetMapping("/encargos")
    public ResponseEntity<ApiResponseDTO<ParametroResponseDTO>> getEncargos() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        ParametroResponseDTO.builder()
                                .percentual(service.getPercentualEncargos())
                                .build(),
                        "Percentual de encargos"
                )
        );
    }
}