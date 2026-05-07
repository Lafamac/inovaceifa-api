package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.ordemservico.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.OrdemServicoService;
import com.inovaceifa.api.swagger.ApiErrorSchema;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/ordens-servico")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "📄 Ordem de Serviço", description = "Gestão de ordens de serviço agrícolas")
public class OrdemServicoController {

    private final OrdemServicoService service;

    /* =========================================================
       LISTAR
       ========================================================= */

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<OrdemServicoResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.listar(pageable),
                        "Ordens de serviço listadas com sucesso"
                )
        );
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrdemServicoResponseDTO>> buscar(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.buscarPorId(id),
                        "Ordem de serviço carregada"
                )
        );
    }

    /* =========================================================
       CRIAR MANUAL
       ========================================================= */

    @PostMapping
    public ResponseEntity<ApiResponseDTO<OrdemServicoResponseDTO>> criar(
            @Valid @RequestBody OrdemServicoCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        service.criar(dto),
                        "Ordem de serviço criada com sucesso"
                ));
    }

    /* =========================================================
       🔥 GERAR A PARTIR DO PLANEJAMENTO
       ========================================================= */

    @Operation(summary = "Gerar Ordem de Serviço a partir de planejamentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordem de serviço gerada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação",
                    content = @Content(schema = @Schema(implementation = ApiErrorSchema.class)))
    })
    @PostMapping("/from-planejamento")
    public ResponseEntity<ApiResponseDTO<OrdemServicoResponseDTO>> gerarDePlanejamento(
            @Valid @RequestBody OrdemServicoFromPlanejamentoDTO dto
    ) {

        OrdemServicoResponseDTO response =
                service.gerarDePlanejamentos(dto.getPlanejamentoIds());

        return ResponseEntity.ok(
                ApiResponseDTO.success(response, "Ordem de serviço gerada a partir do planejamento")
        );
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrdemServicoResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.atualizar(id, dto),
                        "Ordem de serviço atualizada"
                )
        );
    }

    /* =========================================================
       EXCLUIR
       ========================================================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(
            @PathVariable Long id
    ) {
        service.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Ordem de serviço excluída")
        );
    }

    /* =========================================================
   ADICIONAR MÁQUINAS
   ========================================================= */

    @PostMapping("/{id}/maquinas")
    public ResponseEntity<ApiResponseDTO<Void>> adicionarMaquinas(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoMaquinasDTO dto
    ) {

        service.adicionarMaquinas(id, dto);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Máquinas adicionadas com sucesso")
        );
    }

/* =========================================================
   ADICIONAR FUNCIONÁRIOS
   ========================================================= */

    @PostMapping("/{id}/funcionarios")
    public ResponseEntity<ApiResponseDTO<Void>> adicionarFuncionarios(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoFuncionariosDTO dto
    ) {

        service.adicionarFuncionarios(id, dto);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Funcionários adicionados com sucesso")
        );
    }

    /* =========================================================
       FINALIZAR OS
       ========================================================= */

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<ApiResponseDTO<Void>> finalizar(
            @PathVariable Long id
    ) {

        service.finalizarOrdemServico(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Ordem de serviço finalizada com sucesso")
        );
    }
}