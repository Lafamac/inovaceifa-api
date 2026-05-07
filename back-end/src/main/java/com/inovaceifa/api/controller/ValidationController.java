package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validation")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;

    /* =========================================================
       VERIFICAR CPF GLOBAL
       ========================================================= */
    @GetMapping("/verificar-cpf/{cpf}")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarCpf(
            @PathVariable String cpf
    ) {

        boolean disponivel = validationService.cpfDisponivel(cpf);

        return ResponseEntity.ok(
                ApiResponseDTO.success(disponivel, "Consulta realizada com sucesso")
        );
    }

    /* =========================================================
       VERIFICAR CNPJ GLOBAL
       ========================================================= */
    @GetMapping("/verificar-cnpj/{cnpj}")
    public ResponseEntity<ApiResponseDTO<Boolean>> verificarCnpj(
            @PathVariable String cnpj
    ) {

        boolean disponivel = validationService.cnpjDisponivel(cnpj);

        return ResponseEntity.ok(
                ApiResponseDTO.success(disponivel, "Consulta realizada com sucesso")
        );
    }
}
