package com.inovaceifa.api.controller.admin;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.proprietario.ProprietarioResponseDTO;
import com.inovaceifa.api.service.ProprietarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/proprietarios")
@RequiredArgsConstructor
public class AdminProprietarioController {

    private final ProprietarioService proprietarioService;

    /* =========================================================
       LISTAR PROPRIETÁRIOS (SUPER USUÁRIO)
       ========================================================= */

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<ProprietarioResponseDTO>>> listar(
            @PageableDefault(sort = "nome", direction = Sort.Direction.ASC) Pageable pageable
    ) {

        PageResponseDTO<ProprietarioResponseDTO> response =
                proprietarioService.listar(pageable);

        String mensagem = response.getContent().isEmpty()
                ? "Nenhum proprietário encontrado"
                : "Proprietários listados com sucesso";

        return ResponseEntity.ok(
                ApiResponseDTO.success(response, mensagem)
        );
    }

    /* =========================================================
       ATIVAR PROPRIETÁRIO
       ========================================================= */

    @PutMapping("/{id}/ativar")
    public ResponseEntity<ApiResponseDTO<Void>> ativar(@PathVariable Long id) {

        proprietarioService.alterarStatus(id, true);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Proprietário ativado com sucesso")
        );
    }

    /* =========================================================
       DESATIVAR PROPRIETÁRIO
       ========================================================= */

    @PutMapping("/{id}/desativar")
    public ResponseEntity<ApiResponseDTO<Void>> desativar(@PathVariable Long id) {

        proprietarioService.alterarStatus(id, false);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Proprietário desativado com sucesso")
        );
    }
}
