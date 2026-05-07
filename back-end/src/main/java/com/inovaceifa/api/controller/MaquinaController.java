package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.maquina.MaquinaCreateDTO;
import com.inovaceifa.api.dto.maquina.MaquinaResponseDTO;
import com.inovaceifa.api.dto.maquina.MaquinaUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.MaquinaService;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/maquinas")
@RequiredArgsConstructor
@Tag(name = "🚜 Máquinas", description = "Gestão de máquinas agrícolas da fazenda")
@SecurityRequirement(name = "bearerAuth")
public class MaquinaController {

    private final MaquinaService maquinaService;

    /* =========================================================
       LISTAR ATIVAS
       ========================================================= */

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<MaquinaResponseDTO>>> listar(
            @ParameterObject
            @PageableDefault(sort = "nome", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        var response = maquinaService.listar(pageable);

        String msg = response.getContent().isEmpty()
                ? "Nenhuma máquina cadastrada"
                : "Máquinas listadas com sucesso";

        return ResponseEntity.ok(ApiResponseDTO.success(response, msg));
    }

    /* =========================================================
       LISTAR INATIVAS
       ========================================================= */

    @GetMapping("/inativas")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<MaquinaResponseDTO>>> listarInativas(
            @ParameterObject Pageable pageable
    ) {

        var response = maquinaService.listarInativas(pageable);

        return ResponseEntity.ok(
                ApiResponseDTO.success(response, "Máquinas inativas listadas com sucesso")
        );
    }

    /* =========================================================
       BUSCAR POR ID
       ========================================================= */

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MaquinaResponseDTO>> buscar(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        maquinaService.buscarPorId(id),
                        "Máquina carregada com sucesso"
                )
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @PostMapping
    public ResponseEntity<ApiResponseDTO<MaquinaResponseDTO>> criar(
            @Valid @RequestBody MaquinaCreateDTO dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        maquinaService.criar(dto),
                        "Máquina criada com sucesso"
                ));
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MaquinaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MaquinaUpdateDTO dto
    ) {

        return ResponseEntity.ok(ApiResponseDTO.success(
                maquinaService.atualizar(id, dto),
                "Máquina atualizada com sucesso"
        ));
    }

    /* =========================================================
       EXCLUIR (SOFT DELETE)
       ========================================================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(@PathVariable Long id) {

        maquinaService.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Máquina inativada com sucesso")
        );
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {

        maquinaService.reativar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Máquina reativada com sucesso")
        );
    }
}