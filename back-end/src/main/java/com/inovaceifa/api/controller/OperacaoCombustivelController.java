package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.operacaocombustivel.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.OperacaoCombustivelService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "Operação de Combustível",
        description = "Controle de gastos com combustível"
)
@RestController
@RequestMapping("/operacao-combustivel")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class OperacaoCombustivelController {

    private final OperacaoCombustivelService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<OperacaoCombustivelResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(pageable), "Combustível da operação listado")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<OperacaoCombustivelResponseDTO>> criar(
            @Valid @RequestBody OperacaoCombustivelCreateDTO dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(service.criar(dto), "Combustível registrado"));
    }
}