package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.fazenda.FazendaCreateDTO;
import com.inovaceifa.api.dto.fazenda.FazendaResponseDTO;
import com.inovaceifa.api.dto.fazenda.FazendaUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.ContextoFazendaService;
import com.inovaceifa.api.service.FazendaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Fazenda",
        description = "Controle de fazendas"
)
@RestController
@RequestMapping("/fazendas")
@RequiredArgsConstructor
public class FazendaController {

    private final FazendaService fazendaService;
    private final ContextoFazendaService contextoFazendaService;

    /* =========================================================
       LISTAR ATIVAS
       ========================================================= */
    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<FazendaResponseDTO>>> listar(
            @ParameterObject
            @PageableDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        var page = fazendaService.listarMinhasFazendas(pageable);

        String mensagem = page.getContent().isEmpty()
                ? "Nenhuma fazenda cadastrada"
                : "Fazendas listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(page, mensagem));
    }

    /* =========================================================
       LISTAR INATIVAS
       ========================================================= */
    @GetMapping("/inativas")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<FazendaResponseDTO>>> listarInativas(
            @ParameterObject
            @PageableDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        var page = fazendaService.listarInativas(pageable);

        String mensagem = page.getContent().isEmpty()
                ? "Nenhuma fazenda inativa encontrada"
                : "Fazendas inativas listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(page, mensagem));
    }

    /* =========================================================
       CRIAR
       ========================================================= */
    @PostMapping
    public ResponseEntity<ApiResponseDTO<FazendaResponseDTO>> criar(
            @Valid @RequestBody FazendaCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        fazendaService.criarFazenda(dto),
                        "Fazenda criada com sucesso"
                ));
    }

    /* =========================================================
       BUSCAR POR ID
       ========================================================= */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FazendaResponseDTO>> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        fazendaService.buscarPorId(id),
                        "Fazenda carregada com sucesso"
                )
        );
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FazendaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FazendaUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        fazendaService.atualizar(id, dto),
                        "Fazenda atualizada com sucesso"
                )
        );
    }

    /* =========================================================
       SOFT DELETE
       ========================================================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {
        fazendaService.excluir(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Fazenda excluída com sucesso")
        );
    }

    /* =========================================================
       REATIVAR
       ========================================================= */
    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {
        fazendaService.reativar(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Fazenda reativada com sucesso")
        );
    }

    /* =========================================================
       DEFINIR FAZENDA ATIVA
       ========================================================= */
    @PutMapping("/{id}/ativa")
    public ResponseEntity<ApiResponseDTO<Void>> definirAtiva(@PathVariable Long id) {
        contextoFazendaService.definirFazendaAtiva(id);
        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Fazenda ativa definida com sucesso")
        );
    }
}