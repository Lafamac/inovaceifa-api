package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.planejamento.*;
import com.inovaceifa.api.service.PlanejamentoComparativoDetalhadoService;
import com.inovaceifa.api.service.PlanejamentoOperacaoService;
import com.inovaceifa.api.service.PlanejamentoComparativoService;
import com.inovaceifa.api.service.PlanejamentoCalculoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/planejamento-operacao")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "📊 Planejamento", description = "Planejamento de operações por talhão")
public class PlanejamentoOperacaoController {

    private final PlanejamentoOperacaoService service;
    private final PlanejamentoComparativoDetalhadoService comparativoDetalhadoService;
    private final PlanejamentoComparativoService comparativoService;
    private final PlanejamentoCalculoService calculoService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<PlanejamentoOperacaoResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(pageable), "Planejamento listado com sucesso")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<PlanejamentoOperacaoResponseDTO>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(service.buscarPorId(id), "Planejamento carregado")
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<PlanejamentoOperacaoResponseDTO>> criar(
            @Valid @RequestBody PlanejamentoOperacaoCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(service.criar(dto), "Planejamento criado com sucesso"));
    }

    /* ========================= RESUMO ========================= */

    @GetMapping("/{id}/resumo")
    public ResponseEntity<ApiResponseDTO<PlanejamentoResumoDTO>> resumo(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(calculoService.gerarResumo(id), "Resumo gerado com sucesso")
        );
    }

    /* ========================= COMPARATIVO ========================= */

    @GetMapping("/{id}/comparativo")
    public ResponseEntity<ApiResponseDTO<PlanejamentoComparativoDTO>> comparativo(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(comparativoService.comparar(id), "Comparativo gerado com sucesso")
        );
    }

    @GetMapping("/{id}/comparativo-detalhado")
    public ResponseEntity<ApiResponseDTO<PlanejamentoComparativoDetalhadoDTO>> comparativoDetalhado(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        comparativoDetalhadoService.compararDetalhado(id),
                        "Comparativo detalhado gerado"
                )
        );
    }

    @GetMapping("/por-safra-talhao/{id}")
    public ResponseEntity<ApiResponseDTO<PlanejamentoOperacaoResponseDTO>> buscarPorSafraTalhao(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.buscarPorSafraTalhao(id),
                        "Planejamento encontrado"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.ok(ApiResponseDTO.success(null, "Planejamento inativado"));
    }
}