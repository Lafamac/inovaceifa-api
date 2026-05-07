package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.referencia.*;
import com.inovaceifa.api.service.ReferenciaAdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/referencias")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReferenciaAdminController {

    private final ReferenciaAdminService service;

    /* =========================================================
       LISTAR TODAS AS REFERÊNCIAS
    ========================================================= */

    @GetMapping
    public ResponseEntity<ApiResponseDTO<Map<String, List<ReferenciaResponseDTO>>>> listarTodas() {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        service.listarTodas(),
                        "Todas as referências carregadas"
                )
        );
    }

    /* =========================================================
       LISTAR ATIVOS
    ========================================================= */

    @GetMapping("/{tipo}")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaResponseDTO>>> listar(
            @PathVariable String tipo
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listar(tipo), "Referências carregadas")
        );
    }

    /* =========================================================
       LISTAR INATIVOS
    ========================================================= */

    @GetMapping("/{tipo}/inativos")
    public ResponseEntity<ApiResponseDTO<List<ReferenciaResponseDTO>>> listarInativos(
            @PathVariable String tipo
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.listarInativos(tipo), "Referências inativas")
        );
    }

    /* =========================================================
       BUSCAR
    ========================================================= */

    @GetMapping("/{tipo}/{id}")
    public ResponseEntity<ApiResponseDTO<ReferenciaResponseDTO>> buscar(
            @PathVariable String tipo,
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.buscar(tipo, id), "Referência encontrada")
        );
    }

    /* =========================================================
       CRIAR
    ========================================================= */

    @PostMapping("/{tipo}")
    public ResponseEntity<ApiResponseDTO<ReferenciaResponseDTO>> criar(
            @PathVariable String tipo,
            @Valid @RequestBody ReferenciaCreateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.criar(tipo, dto), "Referência criada")
        );
    }

    /* =========================================================
       ATUALIZAR
    ========================================================= */

    @PutMapping("/{tipo}/{id}")
    public ResponseEntity<ApiResponseDTO<ReferenciaResponseDTO>> atualizar(
            @PathVariable String tipo,
            @PathVariable Long id,
            @Valid @RequestBody ReferenciaUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(service.atualizar(tipo, id, dto), "Referência atualizada")
        );
    }

    /* =========================================================
       DESATIVAR
    ========================================================= */

    @DeleteMapping("/{tipo}/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> desativar(
            @PathVariable String tipo,
            @PathVariable Long id
    ) {

        service.desativar(tipo, id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Referência desativada")
        );
    }

    /* =========================================================
       REATIVAR
    ========================================================= */

    @PutMapping("/{tipo}/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(
            @PathVariable String tipo,
            @PathVariable Long id
    ) {

        service.reativar(tipo, id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Referência reativada")
        );
    }

}