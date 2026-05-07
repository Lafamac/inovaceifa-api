package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.operacaofuncionario.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.OperacaoFuncionarioService;
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
        name = "Operação Funcionários",
        description = "Controle de gastos com funcionários"
)
@RestController
@RequestMapping("/operacao-funcionarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class OperacaoFuncionarioController {

    private final OperacaoFuncionarioService service;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<OperacaoFuncionarioResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(pageable), "Funcionários da operação listados")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<OperacaoFuncionarioResponseDTO>> criar(
            @Valid @RequestBody OperacaoFuncionarioCreateDTO dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(service.criar(dto), "Funcionário vinculado à operação"));
    }
}