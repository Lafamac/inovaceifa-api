package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.service.ContextoFazendaService;
import com.inovaceifa.api.swagger.ApiErrorSchema;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contexto")
@RequiredArgsConstructor
@Tag(
        name = "🏡 Contexto da Fazenda",
        description = "Gerencia fazenda e safra ativa do usuário"
)
@SecurityRequirement(name = "bearerAuth")
public class ContextoFazendaController {

    private final ContextoFazendaService contextoFazendaService;

    @PostMapping("/proprietario-ativo/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> definirProprietarioAtivo(
            @PathVariable Long id
    ) {

        contextoFazendaService.definirProprietarioAtivo(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Proprietário ativo definido com sucesso")
        );
    }

    @PostMapping("/fazenda-ativa/{fazendaId}")
    public ResponseEntity<ApiResponseDTO<Void>> definirFazendaAtiva(
            @PathVariable Long fazendaId
    ) {

        contextoFazendaService.definirFazendaAtiva(fazendaId);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Fazenda ativa definida com sucesso")
        );
    }

    @PostMapping("/safra-ativa/{safraId}")
    public ResponseEntity<ApiResponseDTO<Void>> definirSafraAtiva(
            @PathVariable Long safraId
    ) {

        contextoFazendaService.definirSafraAtiva(safraId);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Safra ativa definida com sucesso")
        );
    }
}