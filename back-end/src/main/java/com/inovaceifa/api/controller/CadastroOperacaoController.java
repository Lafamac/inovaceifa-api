package com.inovaceifa.api.controller;

import com.inovaceifa.api.core.BaseController;
import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.operacao.*;
import com.inovaceifa.api.service.CadastroOperacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "Cadastro de Operações",
        description = "Tabela de referência Cadastro de Operações"
)
@RestController
@RequestMapping("/cadastro-operacoes")
@RequiredArgsConstructor
public class CadastroOperacaoController extends BaseController {

    private final CadastroOperacaoService service;

    /* =========================================================
       LISTAR
       ========================================================= */

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<CadastroOperacaoResponseDTO>>> listar(

            @ParameterObject
            @PageableDefault(sort = "operacao", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        var response = service.listar(pageable);

        String msg = response.getContent().isEmpty()
                ? "Nenhuma operação cadastrada"
                : "Operações listadas com sucesso";

        return ok(response, msg);
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    @GetMapping("/ativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<CadastroOperacaoResponseDTO>>> listarAtivos(

            @ParameterObject
            @PageableDefault(sort = "operacao", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        var response = service.listarAtivos(pageable);

        String msg = response.getContent().isEmpty()
                ? "Nenhuma operação ativa encontrada"
                : "Operações ativas listadas com sucesso";

        return ok(response, msg);
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<CadastroOperacaoResponseDTO>>> listarInativos(

            @ParameterObject
            @PageableDefault(sort = "operacao", direction = Sort.Direction.ASC)
            Pageable pageable
    ) {

        var response = service.listarInativos(pageable);

        String msg = response.getContent().isEmpty()
                ? "Nenhuma operação inativa encontrada"
                : "Operações inativas listadas com sucesso";

        return ok(response, msg);
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CadastroOperacaoResponseDTO>> buscar(@PathVariable Long id) {

        return ok(service.buscar(id), "Operação encontrada");
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @PostMapping
    public ResponseEntity<ApiResponseDTO<CadastroOperacaoResponseDTO>> criar(
            @RequestBody CadastroOperacaoCreateDTO dto
    ) {

        return ok(service.criar(dto), "Operação criada com sucesso");
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<CadastroOperacaoResponseDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody CadastroOperacaoUpdateDTO dto
    ) {

        return ok(service.atualizar(id, dto), "Operação atualizada com sucesso");
    }

    /* =========================================================
       DESATIVAR
       ========================================================= */

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> desativar(@PathVariable Long id) {

        service.desativar(id);

        return ok(null, "Operação desativada");
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(@PathVariable Long id) {

        service.reativar(id);

        return ok(null, "Operação reativada");
    }

}