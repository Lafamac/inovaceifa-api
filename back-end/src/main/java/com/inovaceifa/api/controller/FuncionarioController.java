package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.funcionario.FuncionarioCreateDTO;
import com.inovaceifa.api.dto.funcionario.FuncionarioCriarUsuarioDTO;
import com.inovaceifa.api.dto.funcionario.FuncionarioResponseDTO;
import com.inovaceifa.api.dto.funcionario.FuncionarioUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.service.FuncionarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Tag(
        name = "Funcionários",
        description = "Controle de funcionários"
)
@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<FuncionarioResponseDTO>>> listar(
            @ParameterObject Pageable pageable
    ) {

        PageResponseDTO<FuncionarioResponseDTO> response =
                funcionarioService.listar(pageable);

        return ResponseEntity.ok(
                ApiResponseDTO.success(response, "Funcionários listados com sucesso")
        );
    }

    @GetMapping("/inativos")
    public ResponseEntity<ApiResponseDTO<PageResponseDTO<FuncionarioResponseDTO>>> listarInativos(
            @ParameterObject Pageable pageable
    ) {

        PageResponseDTO<FuncionarioResponseDTO> response =
                funcionarioService.listarInativos(pageable);

        return ResponseEntity.ok(
                ApiResponseDTO.success(response, "Funcionários inativos listados com sucesso")
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FuncionarioResponseDTO>> buscar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        funcionarioService.buscar(id),
                        "Funcionário carregado com sucesso"
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<FuncionarioResponseDTO>> criar(
            @Valid @RequestBody FuncionarioCreateDTO dto
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success(
                        funcionarioService.criar(dto),
                        "Funcionário criado com sucesso"
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FuncionarioResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FuncionarioUpdateDTO dto
    ) {

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        funcionarioService.atualizar(id, dto),
                        "Funcionário atualizado com sucesso"
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> excluir(
            @PathVariable Long id
    ) {

        funcionarioService.excluir(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Funcionário inativado com sucesso")
        );
    }

    @PutMapping("/{id}/reativar")
    public ResponseEntity<ApiResponseDTO<Void>> reativar(
            @PathVariable Long id
    ) {

        funcionarioService.reativar(id);

        return ResponseEntity.ok(
                ApiResponseDTO.success(null, "Funcionário reativado com sucesso")
        );
    }

    /* =========================================================
       CRIAR USUÁRIO PARA FUNCIONÁRIO
       ========================================================= */

    @PostMapping("/{id}/criar-usuario")
    public ResponseEntity<ApiResponseDTO<FuncionarioResponseDTO>> criarUsuario(
            @PathVariable Long id,
            @RequestBody FuncionarioCriarUsuarioDTO dto
    ) {

        FuncionarioResponseDTO response =
                funcionarioService.criarUsuarioParaFuncionario(id, dto);

        return ResponseEntity.ok(
                ApiResponseDTO.success(
                        response,
                        "Usuário criado e e-mail enviado ao funcionário"
                )
        );
    }
}