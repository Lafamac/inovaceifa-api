package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.bi.BiComparativoTalhaoResponseDTO;
import com.inovaceifa.api.service.BiService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bi")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BiController {

    private final BiService service;

    /* =========================================================
       🔥 COMPARATIVO TALHÕES
       ========================================================= */

    @GetMapping("/comparativo-talhoes")
    public ResponseEntity<ApiResponseDTO<BiComparativoTalhaoResponseDTO>>
    comparativoTalhoes() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.comparativoTalhoes(),
                        "Comparativo de talhões gerado com sucesso"
                )
        );
    }
}