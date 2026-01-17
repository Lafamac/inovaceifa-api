package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.dto.LoginRequestDTO;
import com.inovaceifa.api.dto.LoginResponseDTO;
import com.inovaceifa.api.dto.MeResponseDTO;
import com.inovaceifa.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * LOGIN
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @RequestBody LoginRequestDTO dto
    ) {

        LoginResponseDTO response = authService.login(dto);

        return ResponseEntity.ok(
                ApiResponseDTO.<LoginResponseDTO>builder()
                        .success(true)
                        .message("Login realizado com sucesso")
                        .data(response)
                        .build()
        );
    }

    /**
     * /ME
     * Retorna o usuário autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<MeResponseDTO>> me() {

        MeResponseDTO response = authService.me();

        return ResponseEntity.ok(
                ApiResponseDTO.<MeResponseDTO>builder()
                        .success(true)
                        .message("Usuário autenticado")
                        .data(response)
                        .build()
        );
    }
}
